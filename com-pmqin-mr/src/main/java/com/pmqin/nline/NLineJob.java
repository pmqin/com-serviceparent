package com.pmqin.nline;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

//http://blog.csdn.net/yhyr_ycy/article/details/52022531
//http://blog.csdn.net/yinhaonefu/article/details/47067979
public class NLineJob {

	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		String input = "E:/Document/Study/Data/ComparatorNum.txt";
		String output = "E:/Document/Study/Data/output";
		System.setProperty("hadoop.home.dir", "D:/Tools/Office/hadoop-2.6.0");
		Configuration conf = new Configuration();

		Job job = new Job(conf, NLineJob.class.getSimpleName());
		job.setInputFormatClass(NLineInputFormat.class);

		final FileSystem fileSystem = FileSystem.get(new URI(output), conf);
		fileSystem.delete(new Path(output), true);
		// 方式一 设置每个InputSplit中划分三条记录
		// configuration.setInt("mapreduce.input.lineinputformat.linespermap",
		// 3);
		// 方式二 设置每个InputSplit中划分三条记录
		// NLineInputFormat.setNumLinesPerSplit(job, 3);
		NLineInputFormat.setNumLinesPerSplit(job, 20);
		job.setNumReduceTasks(5); // Reducer个数为5个

		job.setJarByClass(NLineJob.class);
		job.setMapperClass(NLineSpliteMap.class);
		job.setPartitionerClass(PartitionerByIndex.class);
		job.setReducerClass(NLineSpliteReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	class NLineSpliteMap extends Mapper<LongWritable, Text, Text, LongWritable> {
		int index = (int) (Math.random() * 10);

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			context.write(new Text(line + " " + index), key);
		}
	}

	class PartitionerByIndex extends Partitioner<Text, LongWritable> {

		@Override
		public int getPartition(Text key, LongWritable value, int numPartitions) {
			// TODO Auto-generated method stub
			int index = Integer.parseInt(key.toString().split(" ")[1]);
			return Math.abs(index % numPartitions);
		}
	}

	class NLineSpliteReduce extends Reducer<Text, LongWritable, Text, LongWritable> {
		@Override
		protected void reduce(Text key, Iterable<LongWritable> value, Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			context.write(new Text(key.toString()), new LongWritable(1));
		}
	}

}
