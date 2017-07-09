package com.pmqin.avro.test.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.Utf8;
import org.junit.Test;

public class GenericRecordDataFileAndEncoder {

	protected static   final String recordDelimiter = null;
    protected static  final  boolean    arrayTostring = false;
	public static void main(String[] args) throws IOException {
		
		ClassLoader classLoader = GenericRecordDataFileAndEncoder.class.getClassLoader();
		URL resource = classLoader.getResource("user.avsc");
		String path = resource.getPath();
		System.out.println(path);
		
		
		
		InputStream resourceAsStream = classLoader.getResourceAsStream("user.avsc");
		Schema schema= Schema.parse(resourceAsStream);
		System.out.println(schema.getName()); 
		System.out.println(schema.getType()); 
		
		
		//创建Avro记录的实例，为记录的String字段构建了一个Avro 实例
		GenericRecord datum=new GenericData.Record(schema);
		datum.put("name",new Utf8("pmqin"));
		datum.put("favorite_number",24);
		datum.put("favorite_color",new Utf8("Red"));
		//将记录序列化到输出流中
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		DatumWriter<GenericRecord> writer=new GenericDatumWriter<GenericRecord>(schema);//DatumWriter 将数据对象翻译成Encoder对象可以理解的类型，
		Encoder encoder= EncoderFactory.get().binaryEncoder(out, null); //然后由Encoder写到数据流。binaryEncoder第二个参数是重用的encoder，这里不重用，所用传空  
		//和Encoder不同，DataFileWriter可以将avro数据写入到文件中  
//        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(writer); 
//        dataFileWriter.create(schema,filepath);  
//		   for (User user : userList) {
//			dataFileWriter.append(user);
//		}
//		dataFileWriter.close();
		writer.write(datum,encoder);
		
		encoder.flush();
		out.close();
		
		
		for(org.apache.avro.Schema.Field column : schema.getFields()){
			System.out.println("user.avsc的字段有："+column.schema().toString());
		}
		System.out.println(schema.getField("favorite_number"));
		for(org.apache.avro.Schema.Field column : schema.getFields()){
			System.out.println("user.avsc的字段有："+column.name());
		}
		 
		
		//反序列化 //result.get返回的是utf-8格式，需要调用toString方法，才能和字符串一致。
		DatumReader<GenericRecord> reader=new GenericDatumReader<GenericRecord>(schema);		 
		Decoder decoder=DecoderFactory.get().binaryDecoder(out.toByteArray(),null);  
		GenericRecord result=reader.read(null,decoder);
		System.out.println(result.get("name"));
		System.out.println(result.get("favorite_number"));
		System.out.println(result.get("favorite_color").toString());
		//GenericData.Array<Record> arrayElements =(GenericData.Array<Record>) result.get("name");
		//System.out.println(arrayElements.size());
		
		  ArrayList<String> field = GetSchemaField(result, result.getSchema());
          for (String column : field) {
        	  System.out.println("pmqin-----------"+column);
          }
		
	}
	
//	这里不需要再传入schema，因为schema已经包含在datafile的头信息里 ,但是前後不一樣的schema就需要了 
//    DatumReader<GenericRecord> reader=new GenericDatumReader<GenericRecord>();  
//    //datafile文件的读取类，指定文件和datumreader  
//    DataFileReader<GenericRecord> dataFileReader=new DataFileReader<GenericRecord>(file,reader);  
//    //测试下读写的schema是否一致  
//    Assert.assertEquals(schema,dataFileReader.getSchema());  
//    //遍历GenericRecord  
//    for (GenericRecord record : dataFileReader){  
//        System.out.println("left="+record.get("left")+",right="+record.get("right"));  
//    }  
	
//	新旧版本schema
//	如果从新旧版本的角度取考虑。
//	新版本schema比旧版本schema增加了一个字段
//	1.新版本取读旧版本的数据，使用新版本schema里新增field的默认值
//	2.旧版本读新版本的数据，新版本schema里新增field被旧版本的忽略掉
//	新版本schema比旧版半schema较少了一个字段
//	1.新版本读旧版本的数据，减少的field被新版本忽略掉
//	2.旧版本读新版本的数据，旧版本的schema使用起被删除field的默认值，如果没有就会报错，那么升级旧版本
	/**
	 * 用增加了field的schema取读数据。new GenericDatumReader<GenericRecord>(null, newSchema)，第一个参数为写的schema，第二个参数为读的schema，
由于读的是avro datafile，schema已经在文件的头部指定，所以写的schema可以忽略掉。
	 * @throws IOException
	 */
	@Test  
	public void testAddField() throws IOException {  
	    //将schema从newStringPair.avsc文件中加载  	
		
	    Schema.Parser parser = new Schema.Parser();  
	    Schema newSchema = parser.parse(getClass().getResourceAsStream("/addStringPair.avsc"));  
	    Schema newSchema2 = parser.parse("");//也可以
	    File filepath = new File("data.avro");  
	    DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(null, newSchema);  
	    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(filepath, reader);  
	    //dataFileReader.hasNext();
	    for (GenericRecord record : dataFileReader) {  
	        System.out.println("left=" + record.get("left") + ",right=" + record.get("right") + ",description="  
	                + record.get("description"));  
	    }  
	}  
	
	 private static  ArrayList<String> GetSchemaField(GenericRecord record, Schema scheme) {
	        ArrayList<String> Field = new ArrayList<String>();
	        if (record == null && scheme == null) {
	            //return Field;
	        } else if (record == null && scheme != null) {
	            for (Schema schema : scheme.getTypes()) {
	                if (Schema.Type.RECORD.equals(schema.getType())) {
	                    for (org.apache.avro.Schema.Field column : schema.getFields()) {
	                        if (column.schema().toString().contains("record")) {
	                            secondeSchemaField(column, null, Field);
	                        } else {
	                            //log.info("key :" + column.name() + "; value:" + "");
	                            Field.add("");
	                        }
	                    }
	                    //log.info(schema.toString());
	                }
	            }
	        } else {
	            java.util.List<org.apache.avro.Schema.Field> schemalist = record.getSchema().getFields();
	            for (org.apache.avro.Schema.Field column : schemalist) {
	                if (column.schema().toString().contains("record")) {// if get type is
	                    secondeSchemaField(column, record, Field);
	                } else {
	                    Object var = record.get(column.name());
	                    if (var == null) {
	                        var = "";
	                    }
	                    //log.info("key :" + column.name() + "; value:" + var);
	                    Field.add(var.toString());
	                }
	            }
	        }
	        return Field;
	    }

	    private static ArrayList<String> secondeSchemaField(org.apache.avro.Schema.Field column,
	                                                 GenericRecord record, ArrayList<String> Field) {
	        if (Schema.Type.ARRAY.equals(column.schema().getType())) {
	            GenericData.Array<Record> arrayElements = null;
	            StringBuilder arrayResult = new StringBuilder();
	            if (record != null) {
	                arrayElements = (GenericData.Array<Record>) record.get(column.name());
	                for (Record element : arrayElements) {
	                    if (arrayTostring == false) {
	                        ArrayList<String> sonField = GetSchemaField((GenericRecord) element,
	                                element.getSchema());
	                        if (sonField != null) {
	                            for (String sonString : sonField) {
	                                Field.add(sonString);
	                            }
	                        } else {
	                            Field.add("");
	                        }
	                    } else {
	                        //StringBuilder arrayResult = new StringBuilder();
	                        if (arrayResult.length() == 0) {
	                            arrayResult.append((element == null ? "" : element.toString()));
	                        } else {
	                            arrayResult.append(recordDelimiter)
	                                    .append((element == null ? "" : element.toString()));
	                        }
	                    }
	                }
	                if (arrayTostring == true) {
	                    if (arrayResult.length() != 0) {
	                        Field.add(arrayResult.toString());
	                    } else {
	                        Field.add("");
	                    }
	                }
	            } else {
	                ArrayList<String> sonField = GetSchemaField((GenericRecord) null, column.schema());
	                if (sonField != null) {
	                    for (String sonString : sonField) {
	                        Field.add(sonString);
	                    }
	                } else {
	                    Field.add("");
	                }
	            }
	        } else if (Schema.Type.RECORD.equals(column.schema().getType())) {
	            GenericData.Record arrayElement = null;
	            if (record != null) {
	                arrayElement = (GenericData.Record) record.get(column.name());
	            }
	            ArrayList<String> sonField = GetSchemaField((GenericRecord) arrayElement, column.schema());
	            if (sonField != null) {
	                for (String sonString : sonField) {
	                    Field.add(sonString);
	                }
	            } else {
	                Field.add("");
	            }
	        } else if (Schema.Type.FIXED.equals(column.schema().getType())) {

	        } else if (Schema.Type.MAP.equals(column.schema().getType())) {
	            GenericData.Record arrayElement = null;
	            StringBuilder arrayResult = new StringBuilder();
	            if (record != null) {
	                arrayElement = (GenericData.Record) record.get(column.name());
	            }
	            ArrayList<String> sonField = GetSchemaField((GenericRecord) arrayElement, column.schema());
	            if (sonField != null) {
	                for (String element : sonField) {
	                    if (arrayResult.length() == 0) {
	                        arrayResult.append((element == null ? "" : element.toString()));
	                    } else {
	                        arrayResult.append(recordDelimiter)
	                                .append((element == null ? "" : element.toString()));
	                    }
	                }
	            } else {
	                Field.add("");
	            }
	        } else {
	            GenericData.Record arrayElement = null;
	            if (record != null) {
	                arrayElement = (GenericData.Record) record.get(column.name());
	            }
	            ArrayList<String> sonField = GetSchemaField((GenericRecord) arrayElement, column.schema());
	            if (sonField != null) {
	                for (String sonString : sonField) {
	                    Field.add(sonString);
	                }
	            } else {
	                Field.add("");
	            }
	        }
	        return Field;
	    }
	
	/**
	 * 用减少了field的schema取读取
	 * @throws IOException
	 */
	@Test  
	public void testRemoveField() throws IOException {  
	    //将schema从StringPair.avsc文件中加载  
	    Schema.Parser parser = new Schema.Parser();  
	    Schema newSchema = parser.parse(getClass().getResourceAsStream("/removeStringPair.avsc"));  
	  
	    File file = new File("data.avro");  
	    DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(null, newSchema);  
	    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, reader);  
	    for (GenericRecord record : dataFileReader) {  
	        System.out.println("left=" + record.get("left"));  
	    }  
	}  

}
