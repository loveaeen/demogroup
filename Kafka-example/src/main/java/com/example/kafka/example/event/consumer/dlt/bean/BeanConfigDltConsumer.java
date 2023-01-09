package com.example.kafka.example.event.consumer.dlt.bean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class BeanConfigDltConsumer {
    /**
     * 使用 @bean 配置创建的重试队列
     * @param consumerRecord
     * @param ack
     */
    @KafkaListener(id = "consumer-b-dlt", clientIdPrefix = "consumer-b-dlt", topics = "topic4-b-r", groupId = "mygroup4", concurrency = "1")
    public void listenAllTwo(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println("我是消费者，但是我抛出异常了，看看我是哪个队列的消息-----"+consumerRecord.toString());
        int e = 1/0;
//        ack.acknowledge();
    }

    public void dlt(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack){
        System.out.println("这是 DLT 消费-----"+consumerRecord.toString());
        ack.acknowledge();
    }
}
