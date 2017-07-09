package com.pmqin.rcfile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hcatalog.rcfile.RCFileMapReduceInputFormat;
import org.apache.hcatalog.rcfile.RCFileMapReduceOutputFormat;
public class RCFileJob  extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		     int res = ToolRunner.run(new Configuration(), new RCFileJob(), args);
		    System.exit(res);

	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if(otherArgs.length < 2){
	    	System.out.println("Usage: " +
	    			"hadoop jar RCFileLoader.jar <main class> " +
	    			"-tableName <tableName> -numCols <numberOfColumns> -input <input path> " +
	    			"-output <output path> -rowGroupSize <rowGroupSize> -ioBufferSize <ioBufferSize>");
	    	System.out.println("For test");
	    	System.out.println("$HADOOP jar RCFileLoader.jar edu.osu.cse.rsam.rcfile.mapreduce.LoadTable " +
	    			"-tableName test1 -numCols 10 -input RCFileLoaderTest/test1 " +
	    			"-output RCFileLoaderTest/RCFile_test1");
	    	System.out.println("$HADOOP jar RCFileLoader.jar edu.osu.cse.rsam.rcfile.mapreduce.LoadTable " +
	    			"-tableName test2 -numCols 5 -input RCFileLoaderTest/test2 " +
	    			"-output RCFileLoaderTest/RCFile_test2");
	    	return 2;
		}
		String tableName = "";
		int numCols = 0;
		String inputPath = "";
		String outputPath = "";
		int rowGroupSize = 16 *1024*1024;
		int ioBufferSize = 128*1024;
	    for (int i=0; i<otherArgs.length - 1; i++){
	    	if("-tableName".equals(otherArgs[i])){
	    		tableName = otherArgs[i+1];
	    	}else if ("-numCols".equals(otherArgs[i])){
	    		numCols = Integer.parseInt(otherArgs[i+1]);
	    	}else if ("-input".equals(otherArgs[i])){
	    		inputPath = otherArgs[i+1];
	    	}else if("-output".equals(otherArgs[i])){
	    		outputPath = otherArgs[i+1];
	    	}else if("-rowGroupSize".equals(otherArgs[i])){
	    		rowGroupSize = Integer.parseInt(otherArgs[i+1]);
	    	}else if("-ioBufferSize".equals(otherArgs[i])){
	    		ioBufferSize = Integer.parseInt(otherArgs[i+1]);
	    	}
	    }
	    conf.setInt("hive.io.rcfile.record.buffer.size", rowGroupSize);
	    conf.setInt("io.file.buffer.size", ioBufferSize);
	    
	    Job job = new Job(conf, "RCFile loader: loading table " + tableName + " with " + numCols + " columns");
	    
	    job.setJarByClass(RCFileJob.class);
	    job.setMapperClass(RCMap.class);
	    job.setMapOutputKeyClass(NullWritable.class);
	    job.setMapOutputValueClass(BytesRefArrayWritable.class);
//	    job.setNumReduceTasks(0);
	    
	    FileInputFormat.addInputPath(job, new Path(inputPath));
	   // job.setCombinerClass(cls);
	    job.setOutputFormatClass(RCFileMapReduceOutputFormat.class);
	    RCFileMapReduceOutputFormat.setColumnNumber(job.getConfiguration(), numCols);
	    RCFileMapReduceOutputFormat.setOutputPath(job, new Path(outputPath));
	    RCFileMapReduceOutputFormat.setCompressOutput(job, false);
	    
	    
	    System.out.println("Loading table " + tableName + " from " + inputPath + " to RCFile located at " + outputPath);
	    System.out.println("number of columns:" + job.getConfiguration().get("hive.io.rcfile.column.number.conf"));
	    System.out.println("RCFile row group size:" + job.getConfiguration().get("hive.io.rcfile.record.buffer.size"));
	    System.out.println("io bufer size:" + job.getConfiguration().get("io.file.buffer.size"));
	    
	    return (job.waitForCompletion(true) ? 0 : 1);
	}

}
