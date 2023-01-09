package com.example.kafka.example.event.consumer.dlt.bean;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class RetryTopicConfig {
    @Bean
    public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, String> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .exponentialBackoff(1000,3,Integer.MAX_VALUE) // 指定消息的重试间隔时间，同时也影响了 topic 创建的数量
                .maxAttempts(4) // 该次数是包含了第一次发送的，所以实际上是 3 次重试。也影响了 topic 创建的数量
                .concurrency(1) // 并发消费的数量
                .includeTopic("topic4-b-r") // 如果不指定，会涵盖所有的 topic
                .retryOn(Throwable.class) // 指定重试的异常，如果不是该异常，直接发送到 DLT 里，不用重试了
                .dltHandlerMethod("beanConfigDltConsumer","dlt") // 指定 DLT 消费者 Bean 的接口
                .dltProcessingFailureStrategy(DltStrategy.ALWAYS_RETRY_ON_ERROR) // DLT 的失败策略，这里是一直重试
                .listenerFactory("my-retry-topic-factory") // 可以与 @RetryableTopic 结合使用
                .create(template);
    }
}
