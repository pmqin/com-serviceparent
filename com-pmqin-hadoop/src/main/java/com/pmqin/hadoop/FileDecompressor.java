package com.pmqin.hadoop;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
//http://blog.csdn.net/lastsweetop/article/details/9173061
//默认只有三种org.apache.hadoop.io.compress.GzipCodec,org.apache.hadoop.io.compress.
public class FileDecompressor {

	//如果你想读取一个被压缩的文件的话，首先你得先通过扩展名判断该用哪种codec，可以看下 hadoop深入研究:(七)——压缩 中得对应关系。
	//当然有更简便得办法，CompressionCodecFactory已经帮你把这件事做了，通过传入一个Path调用它得getCodec方法,即可获得相应得codec
	public static void main(String[] args) throws IOException {
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		Path inputPath = new Path(uri);
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		CompressionCodec codec = factory.getCodec(inputPath);
		if (codec == null) {
			System.out.println("No codec found for " + uri);
			System.exit(1);
		}
		//注意看下removeSuffix方法，这是一个静态方法，它可以将文件的后缀去掉，然后我们将这个路径做为解压的输出路
		String outputUri = CompressionCodecFactory.removeSuffix(uri, codec.getDefaultExtension());

		InputStream in = null;
		OutputStream out = null;

		try {
			in = codec.createInputStream(fs.open(inputPath));
			out = fs.create(new Path(outputUri));
			IOUtils.copyBytes(in, out, conf);
		} finally {
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}

	}

}
