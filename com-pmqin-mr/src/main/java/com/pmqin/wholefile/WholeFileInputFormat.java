package com.pmqin.wholefile;

import java.io.IOException;

import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

//mapper需要访问一个文件中的全部内容。即使不分割文件，仍然需要一个RecordReader来读取文件内容作为record的值。
public class WholeFileInputFormat extends FileInputFormat<NullWritable, BytesWritable> {

	

	@Override
	protected boolean isSplitable(JobContext context, Path filename) {
		// TODO Auto-generated method stub
		//return super.isSplitable(context, filename);
		return false;
	}

	@Override
	public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		return new WholeFileRecordReader(split,context);
	}
	 
	//WholeFileInputFormat 中，没有使用键，此处表示为NullWritable，值是文件内容。它定义了2个方法，isSplitable() 返回false，指定文本不被split，createRecordReader()返回一个定制的RecordReader实现。
	public static class WholeFileRecordReader extends  RecordReader<NullWritable, BytesWritable> {
		private static Logger log = Logger.getLogger(WholeFileRecordReader.class);
		
		protected TaskAttemptContext context;
		private FileSplit fileSplit;
		private Configuration configuration;
	    private BytesWritable value = new BytesWritable();
	    private boolean processed = false;
		//private WholeFileInputFormat inputFormat;
		public WholeFileRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException
	     {
			initialize(split,context);
	     }
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
			  log.info("classpath: " + System.getProperty("java.class.path"));
			    ClassLoader loader = WholeFileRecordReader.class.getClassLoader();
			    log.info("PWD: " + System.getProperty("user.dir"));
			    log.info("classloader: " + loader.getClass());
			    log.info("org.apache.avro.Schema: " + loader.getResource("org/apache/avro/Schema.class"));
			    fileSplit=(FileSplit)split;
			    this.configuration = context.getConfiguration();
		}

		@Override
		public synchronized boolean nextKeyValue() throws IOException, InterruptedException {
			 if (!processed){

				 //WholeFIleRecordReader 负责将FileSplit 转换成一条记录，该记录的键是null，值是这个文件的内容
		            byte[] contents = new byte[(int)fileSplit.getLength()];
		            Path file = fileSplit.getPath();
		            FileSystem fileSystem = file.getFileSystem(configuration);

		            try(FSDataInputStream inputStream = fileSystem.open(file)){
		                IOUtils.readFully(inputStream,contents,0,contents.length);
		                value.set(contents,0,contents.length);
		            }

		            processed = true;
		            return true;

		        }
		        return false;
		}

		@Override
		public NullWritable getCurrentKey() throws IOException, InterruptedException {
			 return NullWritable.get();
		}

		@Override
		public BytesWritable getCurrentValue() throws IOException, InterruptedException {
			 return value;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			 return processed ? 1.0f : 0.0f;
		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			
		}
		
      
		
		

	}


}
