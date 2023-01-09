package com.example.kafka.example.interceptor;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public class MyConsumerInterceptor implements ConsumerInterceptor<String, String> {

    @Override
    public void configure(Map<String, ?> configs) {
    }

    /**
     * 这是在{@link org.apache.kafka.clients.consumer.KafkaConsumer#poll(java.time.Duration)}返回记录之前调用的
     * 此方法允许修改消费者记录，在这种情况下将返回新记录。可以从此方法返回的记录数没有限制。即，拦截器可以过滤记录或生成新记录。
     */
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        System.out.println("consumer interceptor ----------- "+records);
        return records;
    }

    /**
     * 当偏移量被提交时调用。
     * 调用者将忽略此方法抛出的任何异常。
     */
    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        System.out.println("consumer interceptor onCommit ------------ "+offsets);
    }

    @Override
    public void close() {
    }
}