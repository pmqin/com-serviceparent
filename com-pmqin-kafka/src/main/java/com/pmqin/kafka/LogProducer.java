package com.pmqin.kafka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import com.pmqin.infrastructure.DateUtil;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

//http://www.cnblogs.com/liuming1992/p/6433055.html
public class LogProducer {

	private Logger logger = Logger.getLogger(LogProducer.class);
	private Producer<String, String> producer;
	private String topicName;
	public static final char[] charts = "qazwsxedcrfvtgbyhnujmikolp1234567890".toCharArray();
	public static final int chartsLength = charts.length;
	
	public LogProducer(String topicName) throws Exception {
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("producer.properties"));
		ProducerConfig config = new ProducerConfig(properties);
		producer = new Producer<String, String>(config);
		this.topicName=topicName;
	}

	/**
	 * 一次发送一条消息 
	 * @param message
	 */
	public void send(String message) {
		// KeyedMessage 的构造方法的参数分别表示 Topic, Partition key, 消息
		KeyedMessage<String, String> km = new KeyedMessage<String, String>(topicName, message,message);		
		//KeyedMessage<String, String> km = new KeyedMessage<String, String>(topicName, message);//也支持重载
		producer.send(km);		

	}
	
	/**
	 * 一次发送一条消息  构造了KEY
	 */
	public void send() {
		
		producer.send(generateKeyedMessage());		

	}

	/**
	 * 批量发送消息   
	 * @param messages
	 */
	public void send(Collection<String> messages) {
	
		if (messages.isEmpty()) {
			return;
		}
		List<KeyedMessage<String, String>> kms = new ArrayList<KeyedMessage<String, String>>();
		for (String entry : messages) {
			KeyedMessage<String, String> km = new KeyedMessage<String, String>(topicName, entry);
			kms.add(km);
		}
		producer.send(kms);
	}

	public void close() {
		producer.close();
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogProducer producer = null;
		try {
			producer = new LogProducer("test-topic");
			System.out.println("自定义主题pmqin，producer.sendMessage()四组，每组5个开始");
			//producer.sendMessage();
			System.out.println("自定义主题pmqin，producer.sendMessage()四组，每组5个结束");
			int i = 0;
			while (true) {
				//producer.send("第1次测试:"+(i)+",Insert Time:"+DateUtil.getTodayAndTime());	
				producer.send();
				Thread.sleep(3000);
				i++;
				System.out.println("第"+i+"条，写入时间："+DateUtil.getTodayAndTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (producer != null) {
				producer.close();
			}
		}

	}
	
	/**
     * 产生一个消息
     *
     * @return
     */
    private  KeyedMessage<String, String> generateKeyedMessage() {
        String key = "key_" + ThreadLocalRandom.current().nextInt(10, 99);
        StringBuilder sb = new StringBuilder();
        int num = ThreadLocalRandom.current().nextInt(1, 5);
        for (int i = 0; i < num; i++) {
            sb.append(generateStringMessage(ThreadLocalRandom.current().nextInt(3, 20))).append(" ");
        }
        String message = sb.toString().trim();
        return new KeyedMessage(topicName, key, message);
    }
    
    /**
     * 产生一个给定长度的字符串
     *
     * @param numItems
     * @return
     */
    private static String generateStringMessage(int numItems) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numItems; i++) {
            sb.append(charts[ThreadLocalRandom.current().nextInt(chartsLength)]);
        }
        return sb.toString();
    }
	
    /**
     * 单独写入
     * @throws InterruptedException
     */
	public void sendMessage() throws InterruptedException {
		//1-4 四组，每组5个
		for (int i = 1; i < 5; i++) {
			List<KeyedMessage<String, String>> kms = new ArrayList<KeyedMessage<String, String>>();
			for (int j = 1; j < 6; j++) {
				kms.add(new KeyedMessage<String, String>("pmqin", j + "", "The group" + i + " message for key " + j));
			}
			producer.send(kms);
		}
	}

}
