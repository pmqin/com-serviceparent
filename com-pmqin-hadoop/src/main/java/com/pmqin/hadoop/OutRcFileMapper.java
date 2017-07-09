package com.pmqin.hadoop;

import java.io.IOException;

import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class OutRcFileMapper extends Mapper<LongWritable, Text, LongWritable, BytesRefArrayWritable> {

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, LongWritable, BytesRefArrayWritable>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String day = context.getConfiguration().get("date");
		if (!line.equals("")) {
			String[] lines = line.split(" ", -1);
			if (lines.length > 3) {
				String time_temp = lines[1];
				// String times = timeStampDate(time_temp);
				String d = "";// times.substring(0, 10);
				if (day.equals(d)) {
					byte[][] record = { lines[0].getBytes("UTF-8"), lines[1].getBytes("UTF-8"),
							lines[2].getBytes("UTF-8"), lines[3].getBytes("UTF-8") };

					BytesRefArrayWritable bytes = new BytesRefArrayWritable(record.length);

					for (int i = 0; i < record.length; i++) {
						BytesRefWritable cu = new BytesRefWritable(record[i], 0, record[i].length);
						bytes.set(i, cu);
					}
					context.write(key, bytes);
				}
			}
		}
	}

}
