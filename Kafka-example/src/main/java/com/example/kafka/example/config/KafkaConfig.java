package com.example.kafka.example.config;

import com.example.kafka.example.interceptor.MyConsumerInterceptor;
import com.example.kafka.example.interceptor.MyProducerInterceptor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

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

    @Bean
    public MyProducerInterceptor myProducerInterceptor() {
        return new MyProducerInterceptor();
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> pf, MyProducerInterceptor myProducerInterceptor, DefaultKafkaConsumerFactory defaultKafkaConsumerFactory) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(pf);
        // 生产的拦截器
        kafkaTemplate.setProducerInterceptor(myProducerInterceptor);
        // 消费的拦截器
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.putAll(defaultKafkaConsumerFactory.getConfigurationProperties());
        configurationProperties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptor.class.getName());
        defaultKafkaConsumerFactory.updateConfigs(configurationProperties);
        return kafkaTemplate;
    }
}
