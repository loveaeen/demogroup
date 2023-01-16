package com.example.kafka.example.event.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/async/{topic}/{retries}")
    public String async(@PathVariable("topic") String topicNumber, @PathVariable("retries") int retries) {
        applicationContext.getBean(Producer.class).asyncInterMethod(topicNumber,retries);
        return "It's always over..";
    }

    @Async
    public void asyncInterMethod(String topicNumber, int retries){
        for (int i = 0; i < retries; i++) {
            kafkaTemplate.send("topic" + topicNumber, "" + i, "test-----" + i);
        }
    }

    @GetMapping("/sync/{topic}/{retries}")
    @Transactional
    public String sync(@PathVariable("topic") String topicNumber, @PathVariable("retries") int retries){
        for (int i = 0; i < retries; i++) {
            kafkaTemplate.send("topic" + topicNumber, "" + i, "test-----" + i);
            System.out.println("成功啦----"+i);
        }
        return "It's over";
    }
}
