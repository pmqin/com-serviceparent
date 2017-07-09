package com.pmqin.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;


public class ZkClientSlave {
	private ZkClient zkClient;
	private static String connectString = "192.168.238.129:2181";
	private static String path = "/root1";
	public static void main(String[] args) {
		ZkClientSlave bootStrap = new ZkClientSlave();
		bootStrap.initialize();

		try {
			Thread.sleep(100000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 初始化zookeeper
	 */
	public void initialize() {

		int connectionTimeout = 50000;

		zkClient = new ZkClient(connectString, connectionTimeout);

		

		 new Thread(new Runnable() {
	            
	            public void run() {
	            
	                zkClient.subscribeDataChanges(path, new IZkDataListener() {
	                    
	                    public void handleDataDeleted(String dataPath) throws Exception {
	                        System.out.println("the node 'dataPath'===>"+dataPath+",deleted");    
	                    }
	                    
	                    public void handleDataChange(String dataPath, Object data) throws Exception {
	                    	
	                        System.out.println("the node 'dataPath'===>"+dataPath+", data has changed.it's data is "+String.valueOf(((User)data).toString()));
	                        
	                    }
	                });
	                
	            }
	            
	        }).start();
	}

}
