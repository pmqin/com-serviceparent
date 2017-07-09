package com.pmqin.orc;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcFile;
import org.apache.hadoop.hive.ql.io.orc.OrcInputFormat;
import org.apache.hadoop.hive.ql.io.orc.Reader;
import org.apache.orc.mapred.OrcStruct;

public class OrcReader {

	public static void main(String[] args) {
		String INPUT = "/user/hive/orc_test.orc";
		Configuration conf = new Configuration();
		Path file_in = new Path(INPUT);
		Reader r = OrcFile.createReader(FileSystem.get(URI.create(INPUT), conf), file_in);
		// OrcRecordReader reader = new OrcRecordReader(r,conf,0,747);
		//OrcInputFormat.class  http://www.javali.org/document/mapreduce_read_orcfile_solution.html
		OrcRecordReader reader = (OrcRecordReader) OrcInputFormat.createReaderFromFile(r, conf, 0, 747); // 我的test.orc文件长度就是747，可以单独写个getFileSize方法获取
		if (reader != null) {
			System.out.println("========record counts : " + reader.numColumns);
			while (reader.nextKeyValue()) {
				OrcStruct data = reader.getCurrentValue();
				System.out.println("fields: " + data.getNumFields());
				for (int i = 0; i < data.getNumFields(); i++) {
					System.out.println("============" + data.getFieldValue(i));
				}
			}

		}
	}

}
