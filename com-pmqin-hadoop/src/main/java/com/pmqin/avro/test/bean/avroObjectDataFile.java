package com.pmqin.avro.test.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class avroObjectDataFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// String name="pmqin";
		// User user1 = new User(name, 10, "red");
		List<User> userList = produceUsers();
		String fileName = "users.avro";
		// serializeAvroToFile(userList, fileName);
		deserializeAvroFromFile(fileName);

		byte[] usersByteArray = serializeAvroToByteArray(userList);
		deserialzeAvroFromByteArray(usersByteArray);

	}

	public static List<User> produceUsers() {
		List<User> userList = new ArrayList<User>();
		// 三种初始化方式
		User user1 = new User("user1", 10, "red");
		User user2 = new User();
		user2.setName("user2");
		user2.setFavoriteNumber(11);
		user2.setFavoriteColor("white");
		User user3 = User.newBuilder().setName("user3").setFavoriteNumber(12).setFavoriteColor("black").build();
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		return userList;
	}

	/**
	 * 把User对象序列化到文件中
	 * 
	 * @param userList
	 * @param fileName
	 * @throws IOException
	 */
	public static void serializeAvroToFile(List<User> userList, String fileName) throws IOException {
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		dataFileWriter.create(userList.get(0).getSchema(), new File(fileName));
		for (User user : userList) {
			dataFileWriter.append(user);
		}
		dataFileWriter.close();
	}

	/**
	 * 从文件中反序列化对象
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void deserializeAvroFromFile(String fileName) throws IOException {
		File file = new File(fileName);
		DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
		DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);
		User user = null;
		System.out.println("----------------deserializeAvroFromFile-------------------");
		while (dataFileReader.hasNext()) {
			user = dataFileReader.next(user);
			System.out.println(user);
		}
	}

	/**
	 * 序列化对象成byte 数组
	 * 
	 * @param userList
	 * @return
	 * @throws IOException
	 */
	public static byte[] serializeAvroToByteArray(List<User> userList) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		dataFileWriter.create(userList.get(0).getSchema(), baos);
		for (User user : userList) {
			dataFileWriter.append(user);
		}
		dataFileWriter.close();
		return baos.toByteArray();
	}

	/**
	 * 从byte数组中反序列化成对象
	 * 
	 * @param usersByteArray
	 * @throws IOException
	 */
	public static void deserialzeAvroFromByteArray(byte[] usersByteArray) throws IOException {
		SeekableByteArrayInput sbai = new SeekableByteArrayInput(usersByteArray);
		DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
		DataFileReader<User> dataFileReader = new DataFileReader<User>(sbai, userDatumReader);
		System.out.println("----------------deserialzeAvroFromByteArray-------------------");
		User readUser = null;
		while (dataFileReader.hasNext()) {
			readUser = dataFileReader.next(readUser);
			System.out.println(readUser);
		}
	}
}
