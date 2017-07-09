package com.pmqin.zk;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;

public class ZkClientTest {

	public static void main(String[] args) throws InterruptedException {
		// 创建会话
		ZkClient zkClient = new ZkClient("192.168.118.128:2181", 5000);
		final String path = "/zk-book";

		// 监测子节点变化
		zkClient.subscribeChildChanges(path, new IZkChildListener() {

			@Override
			public void handleChildChange(String parentPath, List<String> currentChild) throws Exception {
				System.out.println(parentPath + " 's child changed ,currentChilds: " + currentChild);
			}

		});

		// 创建节点
		zkClient.createPersistent(path);
		
		Thread.sleep(1000);
		System.out.println(zkClient.getChildren(path));
		Thread.sleep(1000);
		zkClient.createPersistent(path + "/c1");
		Thread.sleep(1000);
		System.out.println(zkClient.getChildren(path));
		Thread.sleep(1000);
		zkClient.createPersistent(path + "/c2", "123");
		Thread.sleep(1000);
		System.out.println(zkClient.getChildren(path));

		// 删除节点
		Thread.sleep(1000);
		zkClient.delete(path + "/c1");
		//version：节点的版本。如果版本不符无法删除
		//public boolean delete(java.lang.String path, int version) 
		// boolean e1=zkClient.delete(path);//删除当前节点，有子节点无法删除
		// boolean e2=zkClient.deleteRecursive(path);//删除非子节点
		Thread.sleep(1000);
		System.out.println(zkClient.getChildren(path));
		zkClient.delete(path + "/c2");
		System.out.println(zkClient.getChildren(path));
		Thread.sleep(1000);

	}

	
	
	
	/**
	 * 订阅节点的信息改变（创建节点，删除节点，添加子节点）
	 * 
	 * @author pmqin
	 *
	 */
	private static class ZKChildListener implements IZkChildListener {

		public void handleChildChange(String parentPath, List currentChilds) throws Exception {

			System.out.println(parentPath);
			System.out.println(currentChilds.toString());

		}
	}
	
	
	/**
	 * 订阅节点的数据内容的变化
	 * @author pmqin
	 *
	 */
	private static class ZKDataListener implements IZkDataListener{  
		  
        public void handleDataChange(String dataPath, Object data) throws Exception {  
              
            System.out.println(dataPath+":"+data.toString());  
        }  
  
        public void handleDataDeleted(String dataPath) throws Exception {  
            System.out.println(dataPath);  
        }  
    }  
}
