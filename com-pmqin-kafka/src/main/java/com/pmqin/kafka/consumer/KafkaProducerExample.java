package com.pmqin.kafka.consumer;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.pmqin.infrastructure.DateUtil;


public class KafkaProducerExample {

	private static KafkaProducer<String, String> producer;
	private String topicName;
	public KafkaProducerExample(String topicName) throws IOException {
		this.topicName=topicName;
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("producer0901.properties"));		
		producer = new KafkaProducer<>(properties);
		for(int i = 100; i < 200; i++)
		{    
			Integer partition=i%3;//0.10.1.0 提供时间long重载 
            producer.send(new ProducerRecord<String,String>(topicName,"Key"+i, "Value"+Integer.toString(i)+" ,Time:"+DateUtil.getTodayAndTime()));
        	System.out.println("partition:"+partition+",KeyValue="+i+" ,Time:"+DateUtil.getTodayAndTime());
		}
		producer.close();
	   }
	/**
     * 发送一条消息
     * @param message
     */
    public void send(String topic, String message) {
        ProducerRecord record;
        record = new ProducerRecord<>(topic, "", message);
        producer.send(record, new SendCallback(record, 0));
    }
    /**
     * producer回调
     */
    static class SendCallback implements Callback {
        ProducerRecord<String, String> record;
        int sendSeq = 0;

        public SendCallback(ProducerRecord record, int sendSeq) {
            this.record = record;
            this.sendSeq = sendSeq;
        }
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            //send success
            if (null == e) {
                String meta = "topic:" + recordMetadata.topic() + ", partition:"
                        + recordMetadata.topic() + ", offset:" + recordMetadata.offset();
               // logger.info("send message success, record:" + record.toString() + ", meta:" + meta);
                return;
            }
            //send failed
           // logger.error("send message failed, seq:" + sendSeq + ", record:" + record.toString() + ", errmsg:" + e.getMessage());
            if (sendSeq < 1) {
            	producer.send(record, new SendCallback(record, ++sendSeq));
            }
        }
    }
	public static void main(String[] args) throws IOException {
		KafkaProducerExample producer=new KafkaProducerExample("pmqin");

	}

}
