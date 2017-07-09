package com.pmqin.zk;

import java.util.UUID;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

//http://www.cnblogs.com/shengkejava/p/5633801.html  相关API
//http://www.cnblogs.com/yql1986/p/4116483.html
public class ZkClientMaster {
	private ZkClient zkClient;
	private static String connectString = "192.168.238.129:2181";
	private static String path = "/root1";

	public static void main(String[] args) {
		ZkClientMaster bootStrap = new ZkClientMaster();
		bootStrap.initialize();

		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化zookeeper
	 */
	public void initialize() {

		int connectionTimeout = 50000;
		int sessionTimeout = 50000;
		// SerializableSerializer：对象序列化，可转换对象
		// BytesPushThroughSerializer：字节数组序列化
		zkClient = new ZkClient(connectString, sessionTimeout, connectionTimeout, new SerializableSerializer());

		User user = new User();
		user.setId("1");
		user.setName("testUser");
		if (!zkClient.exists(path)) {
			String path1 = zkClient.create(path, user, CreateMode.EPHEMERAL);
			// 输出创建节点的路径
			System.out.println("输出创建节点的路径  :" + path1);
		}

		new Thread(new RootNodeChangeThread()).start();
	}

	/**
	 * 每5s改变一次 'root1'节点的数据
	 * 
	 * 
	 *
	 */
	private class RootNodeChangeThread implements Runnable {

		public void run() {

			while (true) {

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// ignore
				}

				String uuidStr = UUID.randomUUID().toString();

				System.out.println(">>>>>>>>>> 产生随机的 uuid string,'uuidStr'===>" + uuidStr);
				 User user = new User();  
			     user.setId(uuidStr);  
			     user.setName("testUser"+uuidStr);  
				zkClient.writeData(path, user);//更新数据
				 Stat stat = new Stat();  
		         //获取 节点中的对象  
				 //节点详细信息。传递stat对象到readData方法，方法内部会填充stat数据
		         User  readUser = zkClient.readData(path,stat);  
		         System.out.println("获取 节点中的对象  :"+user.getName());  
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				//返回 true表示节点存在 ，false表示不存在
//		        System.out.println("Node exists :" + zkClient.exists(path));
//		        zkClient.delete(path);
//		        System.out.println("Node exists :" + zkClient.exists(path));
//		        Node exists :true
//		        Node exists :false

			}

		}

	}

}
