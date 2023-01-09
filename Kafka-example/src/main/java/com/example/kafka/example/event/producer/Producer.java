package com.example.kafka.example.event.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @GetMapping("/async/{topic}/{retries}")
    @Async
    public String async(@PathVariable("topic") String topicNumber, @PathVariable("retries") int retries) {
        for (int i = 0; i < retries; i++) {
            kafkaTemplate.send("topic" + topicNumber, "" + i, "test-----" + i);
        }
        return "It's over";
    }

    @GetMapping("/sync/{topic}/{retries}")
    public String sync(@PathVariable("topic") String topicNumber, @PathVariable("retries") int retries){
        for (int i = 0; i < retries; i++) {
            kafkaTemplate.send("topic" + topicNumber, "" + i, "test-----" + i);
            System.out.println("成功啦----"+i);
        }
        return "It's over";
    }
}
