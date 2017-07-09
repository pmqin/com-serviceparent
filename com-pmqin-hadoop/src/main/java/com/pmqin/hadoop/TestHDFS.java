package com.pmqin.hadoop;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

//http://blog.csdn.net/lastsweetop/article/details/9001467
//http://www.tuicool.com/articles/aeAVJ3
public class TestHDFS {

	public static String hdfsUrl = "hdfs://192.168.1.106:8020";
	// 此工厂取解析hdfs协议），这个方法只能调用一次，所以要写在静态块中
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}

	public static void main(String[] args) throws IOException {
		// 当需要很多文件时，一个个列出路径是很不便捷的，hdfs提供了一个通配符列出文件的方法，
		// 通过FileSystem的globStatus方法提供了这个便捷，globStatus也有重载的方法，使用PathFilter过滤
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		FileStatus[] status = fs.globStatus(new Path(uri), new RegexExludePathFilter("^.*/1901"));// FileStatus
																									// 封装了hdfs文件和目录的元数据
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listedPaths) {
			System.out.println(p);
		}
	}

	/*
	 * 读取
	 */
	public void testHDFSread() throws Exception { //
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path path = new Path("/test/1.txt");
		InputStream in = null;
		try {
			in = fs.open(path); // 通过FileSystem打开流返回的对象是个FSDataInputStream对象，该类实现了Seekable接口，
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally {
			IOUtils.closeStream(in);
		}
	}

	/*
	 * create HDFS folder 创建一个文件夹
	 */
	public void testHDFSMkdir() throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);

		Path path = new Path("/test");
		fs.mkdirs(path);// mkdirs方法会自动创建所有不存在的父目录
	}

	public void remove() throws IOException {
		Configuration conf = new Configuration();
		String path = "hdfs://192.168.75.130:9000/root/outputdb";
		FileSystem fs = FileSystem.get(conf);
		Path p = new Path(path);
		if (fs.exists(p)) {
			fs.delete(p, true);
			System.out.println("输出路径存在，已删除！");
		}
	}

	/*
	 * create a file 创建一个文件
	 */
	public void testCreateFile() throws Exception { //
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path path = new Path("/test/a.txt");
		FSDataOutputStream out = fs.create(path);
		out.write("hello hadoop".getBytes());
		// fs.append(f)//和普通文件系统一样，也支持apend操作，写日志时最常用
		// 但并非所有hadoop文件系统都支持append，hdfs支持，s3就不支持。
	}

	public void testRenameFile() throws Exception { // rename a file 重命名
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path path = new Path("/test/a.txt");
		Path newPath = new Path("/test/b.txt");
		System.out.println(fs.rename(path, newPath));
	}

	public void testUploadLocalFile1() throws Exception { // upload a local file
															// 上传文件
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path src = new Path("/home/hadoop/hadoop-1.2.1/bin/rcc");
		Path dst = new Path("/test");
		fs.copyFromLocalFile(src, dst);
	}

	// seek方法可跳到文件中的任意位置，我们这里跳到文件的初始位置再重新读一次
	public void FileSystemDouble() throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		FSDataInputStream in = null;
		try {
			in = fs.open(new Path(hdfsUrl));
			IOUtils.copyBytes(in, System.out, 4096, false);
			in.seek(0);
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally {
			IOUtils.closeStream(in);
		}
	}

	/*
	 * // 拷贝本地文件到hdfs的例子
	 */
	public void testUploadLocalFile3() throws Exception {
		String localSrc = "./dd";
		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		OutputStream out = fs.create(new Path(hdfsUrl), new Progressable() {

			public void progress() {
				System.out.print(".");

			}

		});

		IOUtils.copyBytes(in, out, 4096, true);
	}

	/*
	 * 写数据
	 */
	public void testUploadLocalFile2() throws Exception {

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path src = new Path("/home/hadoop/hadoop-1.2.1/bin/rcc");
		Path dst = new Path(hdfsUrl);
		InputStream in = new BufferedInputStream(new FileInputStream(new File("/home/hadoop/hadoop-1.2.1/bin/rcc")));
		FSDataOutputStream out = fs.create(new Path("/test/rcc1"));
		IOUtils.copyBytes(in, out, 4096);
	}

	// FileStatus 封装了hdfs文件和目录的元数据，包括文件的长度，块大小，重复数，修改时间，所有者，权限等信息
	public void testListFIles() throws Exception { // list files under folder

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path dst = new Path(hdfsUrl);
		FileStatus[] files = fs.listStatus(dst);
		for (FileStatus status : files) {
			System.out.println("path = " + status.getPath());
			System.out.println("owner = " + status.getOwner());
			System.out.println("block size = " + status.getBlockSize());
			System.out.println("permission = " + status.getPermission());
			System.out.println("replication = " + status.getReplication());
		}
	}

	public void testGetBlockInfo() throws Exception { // list block info of file
														// 查找文件所在的数据块
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path dst = new Path("/test/rcc");
		FileStatus fileStatus = fs.getFileStatus(dst);
		BlockLocation[] blkloc = fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen()); // 查找文件所在数据块
		for (BlockLocation loc : blkloc) {
			for (int i = 0; i < loc.getHosts().length; i++)
				System.out.println(loc.getHosts()[i]);
			String[] hoStrings = loc.getHosts();
			System.out.println(hoStrings[0]);
		}
		// 查看文件所在hdfs的集群位置

	}

	public void testListingiles() throws Exception { // list block info of file
		String[] args = null;
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		Path[] paths = new Path[args.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path(args[i]);
		}

		FileStatus[] status = fs.listStatus(paths);
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listedPaths) {
			System.out.println(p);
		}
	}

}
