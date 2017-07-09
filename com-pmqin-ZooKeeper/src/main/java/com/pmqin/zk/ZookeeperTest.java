package com.pmqin.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZookeeperTest implements Watcher {
	private static Log log = LogFactory.getLog(ZookeeperHelper.class);
	private CountDownLatch connectedSemaphore = new CountDownLatch(1);
	ZooKeeper zk;

	public static void main(String[] args) {
		String connectString = "192.168.118.128:2181";
		ZookeeperHelper zkoperator = new ZookeeperHelper();
		zkoperator.createConnection(connectString, 3000);

	}

	public void createConnection(String connectString, int sessionTimeout) {

		log.info("开始创建ZooKeeper连接");
		try {
			zk = new ZooKeeper(connectString, sessionTimeout, this);
			connectedSemaphore.await();
		} catch (Exception e) {
			log.info("连接创建失败，发生 IOException");
			e.printStackTrace();
		}
		log.info("连接ZooKeeper成功");
	}

	@Override
	public void process(WatchedEvent event) {
		log.info("收到事件通知：" + event.getState() + "\n");
		log.info("触发了" + event.getType() + "事件！");
		log.info(event.getPath());

		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

	public void createNodes() {
		try {
			// 创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
			zk.create("/root", "mydata".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			// 在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
			zk.create("/root/childone", "childone".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateNodes() {
		try {
			// 取得/root/childone节点下的数据,返回byte[]
			System.out.println(new String(zk.getData("/root/childone", true, null)));
			// 修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
			zk.setData("/root/childone", "childonemodify2".getBytes(), -1);
			System.out.println(new String(zk.getData("/root/childone", true, null)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteNodes() {
		try {
			// 第二个参数为版本，－1的话直接删除，无视版本
			zk.delete("/root/childone", -1);
			zk.delete("/root", -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getNodes() {
		try {
			// 第二个参数为版本，－1的话直接删除，无视版本
			byte[] bytes = zk.getData("/zk_test", true, null);
			System.out.println("data is " + new String(bytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
