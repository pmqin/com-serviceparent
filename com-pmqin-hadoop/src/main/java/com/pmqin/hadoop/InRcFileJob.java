package com.pmqin.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.RCFileInputFormat;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.stringLiteralSequence_return;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextOutputFormat;

import org.apache.hadoop.util.GenericOptionsParser;


public class InRcFileJob  {
	
	private final static String INPUT_PATH = "hdfs://hadoop:9000/hadoop/smallfiles";
	private final static String OUT_PATH = "hdfs://hadoop:9000/hadoop/smallfiles-out";
	public static void main(String[] args) throws Exception {
      
		Configuration conf=new Configuration();
		String[] otherArgs=new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length!=3) {
			System.out.println("error");
			System.exit(1);
		}
		Job job = new Job(conf,"jobname");
		job.setJarByClass(InRcFileJob.class);
		// 设定输入文件为RcFile格式
		job.setInputFormatClass(RCFileInputFormat.class);
		
		// 普通输出
		job.setOutputFormatClass(TextOutputFormat.class);
		// 设置输入路径
		RCFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		// MultipleInputs.addInputPath(job, new Path(srcpath),RCFileInputFormat.class);
		// 输出
		TextOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		// 输出key格式
		job.setOutputKeyClass(Text.class);
		// 输出value格式
		job.setOutputValueClass(NullWritable.class);
		// 设置mapper类
		job.setMapperClass(InRcFileMapper.class);
		// 这里没设置reduce，reduce的操作就是读Text类型文件，因为mapper已经给转换了。
		int code;
		code = (job.waitForCompletion(true)) ? 0 : 1;
	}

	public void run() {
		
	}

}
