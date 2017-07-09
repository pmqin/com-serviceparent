package com.pmqin.wholefile;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.hive.ql.io.RCFileOutputFormat;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.lib.output.*;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SmallFilesToSequenceFileJob extends Configured implements Tool {

	private final static String INPUT_PATH = "hdfs://hadoop:9000/hadoop/smallfiles";
	private final static String OUT_PATH = "hdfs://hadoop:9000/hadoop/smallfiles-out";

	public static void main(String[] args) throws Exception {
		int code = ToolRunner.run(new SmallFilesToSequenceFileJob(), args);
		System.exit(code);

	}

	@Override
	public int run(String[] args) throws Exception {
		Job job = Job.getInstance(this.getConf());
		job.setInputFormatClass(WholeFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		// 如果指定为BLOCK类型，它将一组record压缩，压缩效果自然是BLOCK好
		SequenceFileOutputFormat.setOutputCompressionType(job, SequenceFile.CompressionType.BLOCK);// 默认是RECORD类型，它会按单个的record压缩
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);

		job.setMapperClass(SequenceFileMapper.class);
		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		FileOutputFormat.setOutputPath(job, new Path(OUT_PATH));
		return job.waitForCompletion(true) ? 0 : 1;
	}

	static class SequenceFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {

		private Text filenamekey;

		@Override
		protected void map(NullWritable key, BytesWritable value,
				Mapper<NullWritable, BytesWritable, Text, BytesWritable>.Context context)
				throws IOException, InterruptedException {
			  context.write(filenamekey,value);//這個文件類型的特點,用用文件名key 每個小文件用value
		
		}

		@Override
		protected void setup(Mapper<NullWritable, BytesWritable, Text, BytesWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			InputSplit split = context.getInputSplit();
			Path path = ((FileSplit) split).getPath();
			filenamekey = new Text(path.toString());
		}

	}

}
