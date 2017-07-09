package com.pmqin.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/*
 * 创建节点(异步)
 */
public class CreateSessionASync implements Watcher {
	private static ZooKeeper zooKeeper;
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	public static void main(String[] args) {
		String connectString = "192.168.238.129:2181";
		try {
            zooKeeper = new ZooKeeper(connectString,5000, new CreateSessionASync());
            connectedSemaphore.await();
            System.out.println("getState:"+zooKeeper.getState());
            Thread.sleep(5000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState().equals(Event.KeeperState.SyncConnected)) {
			connectedSemaphore.countDown();
			doBus();
		}else if (event.getType() == EventType.NodeChildrenChanged) {
            try {
                System.out.println("ReGet Child: " + zooKeeper.getChildren(event.getPath(), true));
            } catch (Exception e) {}
        }
		System.out.println("接收内容：" + event.toString());
	}

	private void doBus() {
		//StringCallback cb 回调接口，执行创建操作后，结果以及数据发送到此接口的实现类中
		//Object ctx：自定义回调数据，在回调实现类可以获取此数据
		zooKeeper.create("/note_scot/note_scot_b", "aa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallBack(), "testAsync");
	}
	
	 static class IStringCallBack implements AsyncCallback.StringCallback {

	        @Override
	        public void processResult(int i, String s, Object o, String s2) {
	            System.out.println("i="+i);//创建成功返回0
	            System.out.println("s="+s);//自定义节点名称
	            System.out.println("o="+o);//自定义回调数据
	            System.out.println("s2="+s2);//最终节点名称（顺序节点最终名称与自定义名称不同,如果是PERSISTENT或者EPHEMERAL就是一样的note_scot_b）

	        }
	    }

}
