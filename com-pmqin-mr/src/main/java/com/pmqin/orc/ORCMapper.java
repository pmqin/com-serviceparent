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

public class ORCMapper extends Mapper<NullWritable, OrcStruct, Text, Text> {

	@Override
	protected void map(NullWritable key, OrcStruct value, Mapper<NullWritable, OrcStruct, Text, Text>.Context context)
			throws IOException, InterruptedException {
		
		context.write((Text) value.getFieldValue(1), (Text) value.getFieldValue(2));
	}

}
