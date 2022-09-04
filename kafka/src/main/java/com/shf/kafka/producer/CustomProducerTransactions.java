package com.shf.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class CustomProducerTransactions {
    public static void main(String[] args) {
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();

        // 2. 给 kafka 配置对象添加配置信息
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.120.20:9092,192.168.120.20:9093,192.168.120.20:9094");

        // key,value 序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 设置事务 id（必须），事务 id 任意起名
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction_id_0");

        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        // 初始化事务
        kafkaProducer.initTransactions();

        // 开启事务
        kafkaProducer.beginTransaction();

        try {
            // 4. 调用 send 方法,发送消息
            for (int i = 0; i < 5; i++) {
                // 发送消息
                kafkaProducer.send(new ProducerRecord<>("first", "atguigu " + i));
            }
            // int i = 1 / 0;
            // 提交事务
            kafkaProducer.commitTransaction();
        } catch (Exception e) {
            // 终止事务
            kafkaProducer.abortTransaction();
        } finally {
            // 5. 关闭资源
            kafkaProducer.close();
        }
    }
}
