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
public class DltConsumer {

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            autoCreateTopics = "false",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_DELAY_VALUE)
    @KafkaListener(id = "consumer-dlt", clientIdPrefix = "consumer-dlt", topics = "topic3", groupId = "mygroup3", concurrency = "1")
    public void listenAllOne(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println("我是消费者，但是我抛出异常了，看看我是哪个队列的消息-----"+consumerRecord.toString());
        throw new RuntimeException();
    }

    @DltHandler
    public void dlt(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack){
        System.out.println("这是 DLT 消费-----"+consumerRecord.toString());
        ack.acknowledge();
    }
}
