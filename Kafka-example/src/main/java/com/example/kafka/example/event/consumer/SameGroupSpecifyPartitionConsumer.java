package com.example.kafka.example.event.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 如果你手动指定分区消费的方法，通过 Kafka CLI 你会查不到 active consumers.
 * 这可能是Spring-kafka 3.0.1 edition 的bug
 */
@Component
public class SameGroupSpecifyPartitionConsumer {

    @KafkaListener(id="consumer-1", clientIdPrefix = "consumer-1", topicPartitions = {@TopicPartition(topic = "topic2", partitions = { "0","1" })}, groupId = "mygroup1", concurrency = "1")
    public void listenTop1(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println("这是第一个分区消费者-----"+consumerRecord.toString());
        ack.acknowledge();
    }

    @KafkaListener(id="consumer-2", clientIdPrefix = "consumer-2", topicPartitions = {@TopicPartition(topic = "topic2", partitions = { "2","3" })}, groupId = "mygroup1", concurrency = "1")
    public void listenTop2(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println("这是第二个分区消费者-----"+consumerRecord.toString());
        ack.acknowledge();
    }
}
