package com.pmqin.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;

//http://www.cnblogs.com/shengkejava/p/5663259.html
public class ZooKeeperCurator {

	static CuratorFramework zkclient = null;
    static String nameSpace = "root";// 根节点
    static {
        String zkhost = "192.168.238.129:2181";// zk的host
        RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);// 重试机制
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost).connectionTimeoutMs(5000).sessionTimeoutMs(5000).retryPolicy(rp);
        builder.namespace(nameSpace);
        CuratorFramework zclient = builder.build();
        zkclient = zclient;
        zkclient.start();// 放在这前面执行
        zkclient.newNamespaceAwareEnsurePath("/" + nameSpace);
    }
    public static void main(String[] args) {
		 try {
	            ZooKeeperCurator.watch();
	            while (true) {
	                Thread.sleep(1000);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	}
	public static void watch() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(zkclient, "/", false);
        cache.start();
        System.out.println("监听开始/zk........");
        PathChildrenCacheListener plis = new PathChildrenCacheListener() {
        	@Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("Event here " + event.getType());
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        System.out.println("Data: " + new String(zkclient.getData().forPath(event.getData().getPath())));
                        System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    case CHILD_UPDATED: {
                        System.out.println("Data: " + new String(zkclient.getData().forPath(event.getData().getPath())));
                        System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                    case CHILD_REMOVED: {
                        System.out.println("Data: " + new String(zkclient.getData().forPath(event.getData().getPath())));
                        System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                }
            }

		
		
        };
        // 注册监听
        cache.getListenable().addListener(plis);
    }

}
