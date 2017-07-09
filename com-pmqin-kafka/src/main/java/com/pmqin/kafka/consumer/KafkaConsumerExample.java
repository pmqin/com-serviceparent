package com.pmqin.kafka.consumer;

import java.util.Arrays;
import java.util.*;
import java.util.Properties;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaConsumerExample {

	private String topicName;
	private final KafkaConsumer<String, String> consumer;
	
	public KafkaConsumerExample(String topic, int partitionsNum) throws Exception {
		this.topicName = topic;
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("consumer0901.properties"));		
		this.consumer = new KafkaConsumer<>(properties);
		//consumer.subscribe(Arrays.asList(topicName)); // 本例使用分区副本自动分配策略
	}

	public void start() throws Exception {
		//ConsumerRecords<String, String> records = consumer.poll(2000); // 本例使用200ms作为获取超时时间
		//for (ConsumerRecord<String, String> record : records) {
			// 这里面写处理消息的逻辑，本例中只是简单地打印消息
//			System.out.println(" partition: " + record.partition() + ",the message with offset: " + record.offset()+",record:"+record.key()+":"+record.value() );
//		}
	}
	public static void main(String[] args) throws Exception {
		KafkaConsumerExample consumer=new KafkaConsumerExample("pmqin",3);
		consumer.start();
	}

	

}
