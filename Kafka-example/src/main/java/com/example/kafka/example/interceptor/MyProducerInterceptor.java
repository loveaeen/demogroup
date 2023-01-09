package com.example.kafka.example.interceptor;

import io.micrometer.observation.Observation;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MyProducerInterceptor implements ProducerInterceptor<String, String> {

    @Override
    public void configure(Map<String, ?> configs) {

    }

    /**
     * 在序列化键和值并分配分区之前（可以在这个方法中修改消息数据）。如果此方法抛出异常，则不会调用 onAcknowledgement() 或 close()。
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        System.out.println("producer interceptor before ------------ "+record);
        return record;
    }

    /**
     * 由{@link org.springframework.kafka.core.KafkaTemplate#buildCallback(ProducerRecord, Producer, CompletableFuture, Object, Observation)}调用
     * 当发送到服务器的记录已被确认，或者在记录发送到服务器之前发送失败时调用此方法。
     * 不是消费者消费的时候，而是 kafka 服务器确认收到了。
     *
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        System.out.println("kafka server ack, not consumer ------------- "+metadata+exception);
    }

    @Override
    public void close() {

    }
}
