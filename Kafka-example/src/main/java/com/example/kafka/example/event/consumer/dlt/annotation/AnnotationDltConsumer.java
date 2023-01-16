package com.example.kafka.example.event.consumer.dlt.annotation;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class AnnotationDltConsumer {

    /**
     * 使用注解配置的重试队列
     * 进入重试队列再进入死信队列
     * @param consumerRecord
     * @param ack
     */
    @RetryableTopic(
            attempts = "4", // 该重试次数是包含了第一次发送的，所以实际上是 3 次重试。
            backoff = @Backoff(delay = 1000, multiplier = 2.0), // 创建的 topic 数量
            include = Throwable.class, // 指定重试的异常，如果不是该异常，直接发送到 DLT 里，不用重试了
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_DELAY_VALUE)
    @KafkaListener(id = "consumer-a-dlt", clientIdPrefix = "consumer-a-dlt", topics = "topic3-a-r", groupId = "mygroup3", concurrency = "1")
    public void listenAllOne(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        System.out.println("我是消费者，但是我抛出异常了，看看我是哪个队列的消息-----"+consumerRecord.toString());
        ack.acknowledge();
    }

    @DltHandler
    public void dlt(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack){
        System.out.println("这是 DLT 消费-----"+consumerRecord.toString());
        ack.acknowledge();
    }
}
