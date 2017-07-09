package com.pmqin.orc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.orc.TypeDescription;
import org.apache.orc.mapred.OrcStruct;
import org.apache.orc.mapreduce.OrcInputFormat;
import org.apache.orc.mapreduce.OrcOutputFormat;

//http://www.tuicool.com/articles/73uAfme
//http://blog.csdn.net/liuzhoulong/article/details/52048105
public class ORCReducer extends Reducer<Text, Text, NullWritable, OrcStruct> {

	private TypeDescription schema = TypeDescription.fromString("struct<name:string,mobile:string>");
	private OrcStruct pair = (OrcStruct) OrcStruct.createValue(schema);

	private final NullWritable nw = NullWritable.get();

	@Override
	protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, NullWritable, OrcStruct>.Context output)
			throws IOException, InterruptedException {
		for (Text val : values) {
			pair.setFieldValue(0, key);
			pair.setFieldValue(1, val);
			output.write(nw, pair);
		}
	}

}
