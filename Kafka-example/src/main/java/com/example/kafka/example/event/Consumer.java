package com.example.kafka.example.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(id = "consumer-all", topics = "topic1", groupId = "mygroup", concurrency = "1")
    public void listenAll(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println(consumerRecord.toString());
        ack.acknowledge();
    }

    @KafkaListener(id="consumer-0", topicPartitions = @TopicPartition(topic = "topic1",
            partitionOffsets = {
                    @PartitionOffset(partition = "0", initialOffset = "0")}), groupId = "mygroup", concurrency = "1")
    public void listenTop1(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println(consumerRecord.toString());
        ack.acknowledge();
    }
}
