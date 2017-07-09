package com.pmqin.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hive.ql.io.RCFileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class OutRcFileJob extends Configured implements Tool {
	public static void main(String[] args) throws Exception {

		OutRcFileJob job = new OutRcFileJob();
		ToolRunner.run(job, args);
	}

	public void run() {
		Job job = new Job();
		Configuration conf = job.getConfiguration();
		// 设置每行的列簇数
		RCFileOutputFormat.setColumnNumber(conf, 4);
		job.setJarByClass(OutRcFileJob.class);

		FileInputFormat.setInputPaths(job, new Path(srcpath));
		RCFileOutputFormat.setOutputPath(job, new Path(respath));

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(RCFileOutputFormat.class);

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(BytesRefArrayWritable.class);

		job.setMapperClass(OutPutTestMapper.class);

		conf.set("date", line.getOptionValue(DATE));
		// 设置压缩参数
		conf.setBoolean("mapred.output.compress", true);
		conf.set("mapred.output.compression.codec", "org.apache.hadoop.io.compress.GzipCodec");
		//conf.setClass("mapred.map.output.compression.codec",GzipCodec.class, CompressionCodec.class);  
		code = (job.waitForCompletion(true)) ? 0 : 1;
	}

	public int run(String[] args) throws Exception {
		run();
		return 0;
	}
}
