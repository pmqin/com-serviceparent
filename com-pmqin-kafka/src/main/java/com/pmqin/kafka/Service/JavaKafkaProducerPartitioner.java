package com.pmqin.kafka.Service;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class JavaKafkaProducerPartitioner  implements Partitioner {


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public JavaKafkaProducerPartitioner(VerifiableProperties verifiableProperties) {
		// nothings
	}

	/**
	 * 无参构造函数
	 */
	public JavaKafkaProducerPartitioner() {
		this(new VerifiableProperties());
	}
	@Override
	public int partition(Object key, int numPartitions) {
		try {
			int partitionNum = (key.hashCode() & Integer.MAX_VALUE) % numPartitions;
			System.out.println("key:" + key + "partitionNum:" + partitionNum);
			return Math.abs(Integer.parseInt((String) key) % numPartitions);
			// int num = Integer.valueOf(((String) key).replaceAll("key_", "").trim());
		   //return num % numPartitions;
		} catch (Exception e) {
			return Math.abs(key.hashCode() % numPartitions);
		}

	}

}
