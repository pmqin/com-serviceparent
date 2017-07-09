package com.pmqin.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.security.TokenCache;
import org.apache.hadoop.net.NetworkTopology;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.util.StringUtils;

public class InputBlockSplit implements InputFormat<LongWritable, Text> {

	private static int mySplitSize = 1024;
	private final float SPLIT_SLOP = (float) 1.1;

	private static final PathFilter hiddenFilter = new PathFilter() {

		public boolean accept(Path path) {
			String name = path.getName();
			return !name.startsWith("_") && !name.startsWith(".");//accpet方法，方法返回true时表示被过滤掉
		}

	};

	private static class MultiPathFilter implements PathFilter {
		private List<PathFilter> filters;

		public MultiPathFilter(List<PathFilter> filters) {
			this.filters = filters;
		}

		public boolean accept(Path path) {
			for (PathFilter filter : filters) {
				if (!filter.accept(path)) {
					return false;
				}
			}
			return true;
		}

	}

	public InputSplit[] getSplits(JobConf job, int numSplits) throws IOException {
		System.out.println("come into getSplits");
		// TODO Auto-generated method stub
		FileStatus[] files = listStatus(job);
		for (FileStatus file : files) {
			if (file.isDir()) {
				System.out.println("is Dir()");
			}
		}
		int i = 0;
		ArrayList<FileSplit> splits = new ArrayList<FileSplit>();
		NetworkTopology cluseterMap = new NetworkTopology();
		for (FileStatus file : files) {
			Path path = file.getPath();
			FileSystem fs = path.getFileSystem(job);
			long length = file.getLen();
			BlockLocation[] blkLocation = fs.getFileBlockLocations(file, 0, length);
			if (length != 0) {
				long blockSize = file.getBlockSize();
				System.out.println("blockSize=" + blockSize);
				long splitSize = mySplitSize;
				long bytesRemaining = length;

				while (((double) bytesRemaining) / mySplitSize > SPLIT_SLOP) {
					int blkIndex = getBlockIndex(blkLocation, length - bytesRemaining);
					System.out.println("blkIndex=" + blkIndex);
					splits.add(
							new FileSplit(path, length - bytesRemaining, splitSize, blkLocation[blkIndex].getHosts()));
					bytesRemaining -= mySplitSize;
					System.out.println("bytesRemaining=" + bytesRemaining);
					// i++;
				}

				if (bytesRemaining != 0) {
					splits.add(new FileSplit(path, length - bytesRemaining, bytesRemaining,
							blkLocation[blkLocation.length - 1].getHosts()));
					// i++;
				}
			} else if (length != 0) {
				// i++;
				splits.add(new FileSplit(path, 0, length, blkLocation[0].getHosts()));
			} else {
				// i++;
				splits.add(new FileSplit(path, 0, length, new String[0]));
			}
			System.out.print("length=" + length + "block=" + length / 1024);
		}
		return splits.toArray(new FileSplit[splits.size()]);
	}

	 private int getBlockIndex(BlockLocation[] blkLocation, long offset) {  
	        // TODO Auto-generated method stub  
	        for(int i = 0;i < blkLocation.length;i++){  
	            if((blkLocation[i].getOffset()<=offset)&&  
	                    (offset<blkLocation[i].getOffset()+  
	                            blkLocation[i].getLength())){  
	                return i;  
	            }  
	        }  
	        BlockLocation last = blkLocation[blkLocation.length-1];  
	        long fileLength = last.getOffset()+last.getLength()-1;  
	        throw new IllegalArgumentException("Offset"  
	                + offset+"is outside file (0..."+  
	                fileLength+")");  
	    }  
	 private FileStatus[] listStatus(JobConf job) throws IOException {  
	        // TODO Auto-generated method stub  
	        Path[] dirs=getInputPath(job);  
	        if(dirs.length==0){  
	            throw new IOException("No Input FIle");  
	        }  
	        TokenCache.obtainTokensForNamenodes(job.getCredentials(), dirs, job);  
	          
	        List<FileStatus> result = new ArrayList<FileStatus>();  
	        List<IOException> errors = new ArrayList<IOException>();  
	      
	          
	        List<PathFilter> filters = new ArrayList<PathFilter>();  
	        filters.add(hiddenFilter);  
	        PathFilter jobFilter = getInputPathFilter(job);  
	        if(jobFilter!=null){  
	            filters.add(jobFilter);  
	        }  
	        PathFilter inputFilter= new MultiPathFilter(filters);  
	        for(Path p:dirs){  
	            FileSystem fs =p.getFileSystem(job);  
	            FileStatus[] matches = fs.globStatus(p,inputFilter);  
	            if(matches == null){  
	                errors.add(new IOException("matches=null error"));  
	            }else if(matches.length==0){  
	                errors.add(new IOException("matches.length==0 error"));  
	            }else{  
	                for(FileStatus globStat:matches){  
	                    if(globStat.isDir()){  
	                        for(FileStatus stat:fs.listStatus(globStat.getPath(),  
	                                inputFilter)){  
	                            result.add(stat);  
	                              
	                        }  
	                    }else{  
	                        result.add(globStat);  
	                    }  
	                }  
	            }  
	        }  
	        return result.toArray(new FileStatus[result.size()]);  
	    }  
	 
	 private PathFilter getInputPathFilter(JobConf job) {  
	        // TODO Auto-generated method stub  
	        Configuration conf =job;  
	        Class<?> filterClass =conf.getClass("mapred.input"  
	                + ".pathFilter.class",null,PathFilter.class);  
	        return (filterClass!=null)?  
	                (PathFilter)ReflectionUtils.newInstance(filterClass,conf):null;  
	    }  
	  
	    private Path[] getInputPath(JobConf job) {  
	        // TODO Auto-generated method stub  
	        String dirs = job.get("mapred.input.dir","");  
	        String[] list = StringUtils.split(dirs);  
	        Path[] result = new Path[list.length];  
	        for(int i = 0;i<list.length;i++){  
	            result[i]=new Path(StringUtils.unEscapeString(list[i]));  
	        }  
	        return result;  
	    }  
	public RecordReader<LongWritable, Text> getRecordReader(InputSplit split, JobConf job, Reporter reporter)
			throws IOException {
		return new BlockReadRecord(split, job);
	}

}
