package com.pmqin.rcfile;

import java.io.IOException;

import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class RCMap extends Mapper<Object, Text, NullWritable, BytesRefArrayWritable> {
	private byte[] fieldData;
	private int numCols;
	private BytesRefArrayWritable bytes;

	@Override
	protected void map(Object key, Text value,
			Mapper<Object, Text, NullWritable, BytesRefArrayWritable>.Context context)
			throws IOException, InterruptedException {
		bytes.clear();
		String[] cols = value.toString().split("\\|");
		System.out.println("SIZE : "+cols.length);
		for (int i=0; i<numCols; i++){
        	fieldData = cols[i].getBytes("UTF-8");
        	BytesRefWritable cu = null;
            cu = new BytesRefWritable(fieldData, 0, fieldData.length);
            bytes.set(i, cu);
		}
		context.write(NullWritable.get(), bytes);
	}

	@Override
	protected void setup(Mapper<Object, Text, NullWritable, BytesRefArrayWritable>.Context context)
			throws IOException, InterruptedException {
		numCols = context.getConfiguration().getInt("hive.io.rcfile.column.number.conf", 0);
		bytes = new BytesRefArrayWritable(numCols);
	}

	@Override
	public void run(Mapper<Object, Text, NullWritable, BytesRefArrayWritable>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.run(context);
	}

}
