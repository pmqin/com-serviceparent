package com.pmqin.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;

import com.pmqin.avro.test.bean.Mail;
import com.pmqin.avro.test.bean.Message;

public class AvroRPCServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting server");
		startServer();
		Thread.sleep(1000);  
		System.out.println("Server started");

//		NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(65111));
//
//		// //获取Mail接口的proxy实现
//
//		Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);
//
//		System.out.println("Client built, got proxy");
//
//		// fill in the Message record and send it
//
//		Message message = new Message();
//		message.setTo(new Utf8("127.0.0.1"));
//
//		message.setFrom(new Utf8("127.0.0.1"));
//
//		message.setBody(new Utf8("this is my message"));
//
//		System.out.println("Calling proxy.send with message: " + message.toString());
//		//底层给服务器发送send方法调用  
//		System.out.println("Result: " + proxy.send(message));
//
//		// cleanup
//
//		client.close();
		Thread.sleep(60 * 1000);
		server.close();
		System.out.println("Server close");
	}

	private static Server server;

	private static void startServer() throws IOException {

		server = new NettyServer(new SpecificResponder(Mail.class, new MailImpl()), new InetSocketAddress(65111));

	}

	public static class MailImpl implements Mail {

		public CharSequence send(Message message) throws AvroRemoteException {

			System.out.println("Message Received pmqin：" + message);
			return "Received your message： " + message.getFrom().toString() + " with body "
					+ message.getBody().toString();
		}

	}

}
