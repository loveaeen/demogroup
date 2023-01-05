package com.example.kafka.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic1")
                .partitions(4)
                .replicas(1)
                .build();
    }
}
