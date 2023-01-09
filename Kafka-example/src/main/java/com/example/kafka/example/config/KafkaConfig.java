package com.example.kafka.example.config;

import com.example.kafka.example.interceptor.MyConsumerInterceptor;
import com.example.kafka.example.interceptor.MyProducerInterceptor;
import io.micrometer.observation.Observation;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("topic1")
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("topic2")
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic3() {
        return TopicBuilder.name("topic3-a-r")
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic4() {
        return TopicBuilder.name("topic4-b-r")
                .partitions(4)
                .replicas(1)
                .build();
    }

    /**
     * 定制更适合自己的 kafkaTemplate ，比如添加拦截器
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> pf, DefaultKafkaConsumerFactory defaultKafkaConsumerFactory) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(pf);
        // 生产的拦截器
        kafkaTemplate.setProducerInterceptor(new MyProducerInterceptor());
        // 消费的拦截器
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.putAll(defaultKafkaConsumerFactory.getConfigurationProperties());
        configurationProperties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptor.class.getName());
        defaultKafkaConsumerFactory.updateConfigs(configurationProperties);
        // 添加生产的监听器
        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
            /**
             * 在成功发送消息后调用（即，在消息被代理确认后）。
             * 在拦截器之后执行
             */
            @Override
            public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
                System.out.println("### Callback :: " + recordMetadata.topic() + " ; partition = "
                        + recordMetadata.partition()  +" with offset= " + recordMetadata.offset()
                        + " ; and value = "+producerRecord.value()+", Timestamp : " + recordMetadata.timestamp() + " ; Message Size = " + recordMetadata.serializedValueSize());
            }

            /**
             * 在拦截器之后执行，生产者发送消息失败时调用
             */
            @Override
            public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                System.out.println("### Topic = " + producerRecord.topic() + " ; Message = " + producerRecord.value());
                exception.printStackTrace();
            }
        });
        return kafkaTemplate;
    }
}
