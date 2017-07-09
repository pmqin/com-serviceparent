package com.pmqin.kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

//http://blog.csdn.net/qq_35799003/article/details/52224865  何时使用高级别API开发
public class LogConsumer {

	private ConsumerConfig config;
	private String topic;
	private int numThreads;//线程数量，一般就是Topic的分区数量
	private MessageExecutor messageExecutor;
	private ConsumerConnector connector;
	private ExecutorService threadPool;

	

	public LogConsumer(String topic, int numThreads, MessageExecutor executor) throws Exception {
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("consumer.properties"));
		config = new ConsumerConfig(properties);
		this.topic = topic;
		this.numThreads = numThreads;
		this.messageExecutor = executor;
	}

	public void start() throws Exception {
		 
		// 1. 创建Kafka连接器
		 connector = Consumer.createJavaConsumerConnector(config);
		 
		 Map<String, Integer> topics = new HashMap<String, Integer>();
		 topics.put(topic, numThreads);
		// 2. 自定义指定数据的解码器
//	        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
//	        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
	     // 3. 获取连接数据的迭代器对象集合
	        /**
	         * Key: Topic主题
	         * Value: 对应Topic的数据流读取器，大小是topicCountMap中指定的topic大小
	         */
	     //Map<String, List<KafkaStream<String, String>>> consumerMap = this.consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
		 Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topics);
		 List<KafkaStream<byte[], byte[]>> partitions = streams.get(topic);//获取多个分区的数据
		
		 threadPool = Executors.newFixedThreadPool(numThreads);//固定线程数来消费
		 // 6. 构建数据输出对象
	    int threadNumber = 0;
		 for (KafkaStream<byte[], byte[]> partition : partitions) {			 
		       threadPool.execute(new MessageRunner(partition));
		       threadNumber++;
		 }
	}

	public void close() {
		try {
			//threadPool.shutdownNow();
			if (threadPool != null)
			threadPool.shutdown();
		} catch (Exception e) {
			//
		} finally {
			if (connector != null)
			connector.shutdown();
		}

	}

	//迭代循环分区消息
	class MessageRunner implements Runnable {
		private KafkaStream<byte[], byte[]> partition;

		MessageRunner(KafkaStream<byte[], byte[]> partition) {
			this.partition = partition;
		}

		public void run() {
			ConsumerIterator<byte[], byte[]> it = partition.iterator();
			while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> item = it.next();
				System.out.println("partiton:" + item.partition()+"，offset:" + item.offset());			
				messageExecutor.execute(new String(item.message()));// UTF-8
				 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogConsumer consumer = null;
		try {
			MessageExecutor executor = new MessageExecutor() {

				public void execute(String message) {
					System.out.println("--消费--" + message);

				}
			};
			consumer = new LogConsumer("test-topic", 2, executor);
			consumer.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		try {    
            Thread.sleep(10000);    
        } catch (InterruptedException ie) {    
     
        }    
		//consumer.close();//是否消费后关闭
	}

}
