package com.pmqin.orc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.orc.mapred.OrcStruct;
import org.apache.orc.mapreduce.OrcInputFormat;
import org.apache.orc.mapreduce.OrcOutputFormat;

public class ORCFileJob {

	//http://www.tuicool.com/articles/73uAfme  http://blog.csdn.net/liuzhoulong/article/details/52048105
	public static void main(String[] args) throws Exception  {
		Configuration conf = new Configuration();
		conf.set("orc.mapred.output.schema", "struct<name:string,mobile:string>");
		Job job = Job.getInstance(conf, "ORC Test");
		job.setJarByClass(ORCFileJob.class);
		job.setMapperClass(ORCMapper.class);
		job.setReducerClass(ORCReducer.class);
		job.setInputFormatClass(OrcInputFormat.class);
		job.setOutputFormatClass(OrcOutputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(OrcStruct.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
