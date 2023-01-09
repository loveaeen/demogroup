package com.example.kafka.example.event.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SameGroupConsumer {

    @KafkaListener(id = "consumer-all1", clientIdPrefix = "consumer-all1", topics = "topic1", groupId = "mygroup", concurrency = "1")
    public void listenAllOne(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println("这是第一个消费者，但是我拒绝了，并且会无限处理-----"+consumerRecord.toString());
        ack.nack(Duration.ofMillis(1000));
    }

    @KafkaListener(id = "consumer-all2", clientIdPrefix = "consumer-all2", topics = "topic1", groupId = "mygroup", concurrency = "1")
    public void listenAllTwo(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
//        System.out.println("这是第二个消费者-----"+consumerRecord.toString());
        ack.acknowledge();
    }

    @KafkaListener(id = "consumer-all3", clientIdPrefix = "consumer-all3", topics = "topic1", groupId = "mygroup", concurrency = "1")
    public void listenAllThree(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
//        System.out.println("这是第三个消费者-----"+consumerRecord.toString());
        ack.acknowledge();
    }

    @KafkaListener(id = "consumer-all4", clientIdPrefix = "consumer-all4", topics = "topic1", groupId = "mygroup", concurrency = "1")
    public void listenAllFour(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
//        System.out.println("这是第四个消费者-----"+consumerRecord.toString());
        ack.acknowledge();
    }
}
