package com.pmqin.zk;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperHelper implements Watcher {
	private static Log log = LogFactory.getLog(ZookeeperHelper.class);
	private CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private ZooKeeper zooKeeper;
	private static Stat stat = new Stat();
	private static String connectString = "192.168.118.128:2181";

	public static void main(String[] args) throws KeeperException, InterruptedException {

		ZookeeperHelper zkoperator = new ZookeeperHelper();
		zkoperator.createConnection(connectString, 3000);
		byte[] data = new byte[] { 'a', 'b', 'c', 'd' };
		// zkoperator.create("/note_scot", data);
		// System.out.println(Arrays.toString(zkoperator.getData("/root")));

		// zkoperator.create("/root/child1", data);
		// System.out.println(Arrays.toString(zkoperator.getData("/root/child1")));
		//
		// zkoperator.create("/root/child2", data);
		// System.out.println(Arrays.toString(zkoperator.getData("/root/child2")));
		String zktest = "ZooKeeper的Java API测试";
		// zkoperator.create("/root/child3", zktest.getBytes());
		// log.info("获取设置的信息：" + new
		// String(zkoperator.getData("/root/child3")));

		System.out.println("节点孩子信息:");
		zkoperator.getChild("/root");

		zkoperator.close();
	}

	private void doBus() {
		try {
			if (null != zooKeeper.exists("/note_scot/note_scot_a", false)) {
				System.out.println("/note_scot/note_scot_a 节点已存在");
				return;
			}
			String path = zooKeeper.create("/note_scot/note_scot_a", "aa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);

			/*
			 * 权限相关 try { ACL aclIp = new ACL(ZooDefs.Perms.READ,new
			 * Id("ip","127.0.0.1")); ACL aclDigest = new
			 * ACL(ZooDefs.Perms.READ| ZooDefs.Perms.WRITE, new Id("digest",
			 * DigestAuthenticationProvider.generateDigest("id:pass")));
			 * zooKeeper.addAuthInfo("digest", "id:pass".getBytes()); } catch
			 * (NoSuchAlgorithmException e) { e.printStackTrace(); }
			 */

			System.out.println("zookeeper return:" + path);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建ZK连接
	 * 
	 * @param connectString
	 *            ZK服务器地址列表
	 * @param sessionTimeout
	 *            Session超时时间
	 */
	public void createConnection(String connectString, int sessionTimeout) {

		log.info("开始创建ZooKeeper连接");
		try {
			zooKeeper = new ZooKeeper(connectString, sessionTimeout, this);
			connectedSemaphore.await();

		} catch (Exception e) {
			log.info("连接创建失败，发生 IOException");
			e.printStackTrace();
		}
		log.info("连接ZooKeeper成功" + zooKeeper.getState());
	}

	@Override
	public void process(WatchedEvent event) {
		log.info("收到事件通知：" + event.getState() + "\n");
		log.info("触发了" + event.getType() + "事件！");
		log.info(event.getPath());
		try {
			if (KeeperState.SyncConnected == event.getState()) {// 表示完成初始化
				if (EventType.None == event.getType() && null == event.getPath()) {
					connectedSemaphore.countDown();
					doBus();
				} else if (EventType.NodeCreated == event.getType()) {
					System.out.println("Node(" + event.getPath() + ")Created");
					zooKeeper.exists(event.getPath(), true);
				} else if (EventType.NodeDeleted == event.getType()) {
					System.out.println("Node(" + event.getPath() + ")Deleted");
					zooKeeper.exists(event.getPath(), true);
				} else if (EventType.NodeDataChanged == event.getType()) {
					System.out.println("Node(" + event.getPath() + ")DataChanged");
					zooKeeper.exists(event.getPath(), true);
				}

			} else if (event.getType() == EventType.NodeChildrenChanged) {

				System.out.println("ReGet Child: " + zooKeeper.getChildren(event.getPath(), true));
				// 同步读取节点数据
				System.out.println(new String(zooKeeper.getData(event.getPath(), true, stat)));
				System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());

			}
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * <b>function:</b>创建持久态的znode,比支持多层创建.比如在创建/parent/child的情况下,无/parent.无法通过
	 * 
	 * @author cuiran
	 * @createDate 2013-01-16 15:08:38
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void create(String path, byte[] data) throws KeeperException, InterruptedException {
		/**
		 * 此处采用的是CreateMode是PERSISTENT 表示The znode will not be automatically
		 * deleted upon client's disconnect. EPHEMERAL 表示The znode will be
		 * deleted upon the client's disconnect.
		 */
		this.zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	/**
	 * 
	 * <b>function:</b>获取节点信息
	 * 
	 * @author cuiran
	 * @createDate 2013-01-16 15:17:22
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void getChild(String path) throws KeeperException, InterruptedException {
		try {
			List<String> list = this.zooKeeper.getChildren(path, false);
			if (list.isEmpty()) {
				log.info(path + "中没有节点");
			} else {
				log.info(path + "中存在节点");
				for (String child : list) {
					log.info("节点为：" + child + ":" + new String(this.getData(path + "/" + child)));
				}
			}
		} catch (KeeperException.NoNodeException e) {
			// TODO: handle exception
			throw e;

		}
	}

	public byte[] getData(String path) throws KeeperException, InterruptedException {
		return this.zooKeeper.getData(path, false, null);
	}

	public void close() throws InterruptedException {
		zooKeeper.close();
		log.info("关闭了ZooKeeper连接");
	}

}
