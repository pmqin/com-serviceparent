package com.pmqin.avro.test.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class avroObjectEncoder {

	public static void main(String[] args) throws IOException {
		User user2=new User();  
		user2.setName("user2");
		user2.setFavoriteNumber(11);
		user2.setFavoriteColor("white");
  
        ByteArrayOutputStream out=new ByteArrayOutputStream();  
        //不再需要传schema了，直接用StringPair作为范型和参数，  
        DatumWriter<User> writer=new SpecificDatumWriter<User>(User.class);  
        Encoder encoder= EncoderFactory.get().binaryEncoder(out,null);  
        writer.write(user2, encoder);  
        encoder.flush();  
        out.close();  
  
        DatumReader<User> reader=new SpecificDatumReader<User>(User.class);  
        Decoder decoder= DecoderFactory.get().binaryDecoder(out.toByteArray(),null);  
        User result=reader.read(null,decoder);  
        System.out.println(result.get("name"));
		System.out.println(result.get("favorite_number"));
		System.out.println(result.get("favorite_color").toString());

	}

}
