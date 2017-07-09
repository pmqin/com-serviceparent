package com.pmqin.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 异步读取节点
 * 
 * @author pmqin
 *
 */
public class ZKAsyncTest implements Watcher {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zooKeeper = null;
	private static Stat stat = new Stat();

	public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
		String path = "/zk-book2";
		zooKeeper = new ZooKeeper("192.168.118.128:2181", 5000, new ZKAsyncTest());
		connectedSemaphore.await();

		zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println(new String(zooKeeper.getData(path, true, stat)));
		System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
		
		zooKeeper.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		zooKeeper.getChildren(path, true, new IChildren2CallBack(), null);
		zooKeeper.create(path + "/c2", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		zooKeeper.getData(path, true, new IDataCallBack(), null);
		zooKeeper.setData(path, "1234".getBytes(), -1);
		 System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
		 try {
	            zooKeeper.setData(path, "456".getBytes(), stat.getVersion());
	        } catch (KeeperException e) {
	            System.out.println("Error: " + e.code() + ", " + e.getMessage());
	        }
		 //异步更新节点数据内容
		 zooKeeper.setData(path, "456".getBytes(), -1, new IStatCallBack(), null);
		Thread.sleep(Integer.MAX_VALUE);

	}

	@Override
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			} else if (event.getType() == EventType.NodeChildrenChanged) {
				try {
					System.out.println("ReGet Child: " + zooKeeper.getChildren(event.getPath(), true));
					zooKeeper.getData(event.getPath(), true, new IDataCallBack(), null);
				} catch (Exception e) {
				}
			}
		}
	}

	//异步读取节点
	static class IChildren2CallBack implements AsyncCallback.Children2Callback {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
			System.out.println("Get Children znode result: [response code : " + rc + ", param path: " + path + ",ctx: "
					+ ctx + ", children list: " + children + ",state:" + stat + "]");
		}
	}

	// 异步读取节点数据
	static class IDataCallBack implements AsyncCallback.DataCallback {
		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			System.out.println(rc + ", " + path + ", " + new String(data));
			System.out.println(stat.getCzxid() + "," + stat.getMzxid() + ", " + stat.getVersion());
		}
	}
	//异步更新节点数据内容
	static class IStatCallBack implements AsyncCallback.StatCallback{
	    @Override
	    public void processResult(int rc, String path, Object ctx, Stat stat) {
	        if (rc == 0) {
	            System.out.println("SUCCESS");
	        }
	    }
	}
}
