package com.pmqin.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.util.Utf8;

import com.pmqin.avro.test.bean.Mail;
import com.pmqin.avro.test.bean.Message;

public class AvroRPCClient {

	public static void main(String[] args) throws IOException {
		NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(65111));

		// //获取Mail接口的proxy实现

		Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);

		System.out.println("Client built, got proxy");

		// fill in the Message record and send it

		Message message = new Message();
		message.setTo(new Utf8("127.0.0.1"));

		message.setFrom(new Utf8("127.0.0.1"));

		message.setBody(new Utf8("this is my message"));

		System.out.println("Calling proxy.send with message: " + message.toString());
		//底层给服务器发送send方法调用  
		System.out.println("Result: " + proxy.send(message));

		// cleanup

		client.close();

	}

}
