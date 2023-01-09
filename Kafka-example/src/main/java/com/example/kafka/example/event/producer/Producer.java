package com.example.kafka.example.event.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/kafka")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @GetMapping("/async")
    public String async() {
        for (int i = 0; i < 100; i++) {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("topic2", "" + i, "test-----" + i);
            future.whenComplete( (result ,ex) -> {
                if (ex == null) {
                    System.out.println(result);
                }
                else {
                    System.out.println(ex);
                }
            });
        }
        return "It's over";
    }

    @GetMapping("/sync/{topic}/{retries}")
    public String sync(@PathVariable("topic") String topicNumber, @PathVariable("retries") int retries){
        for (int i = 0; i < retries; i++) {
            try {
                kafkaTemplate.send("topic"+topicNumber, "" + i, "test-----" + i).get(5, TimeUnit.SECONDS);
                System.out.println("成功啦----"+i);
            }catch (ExecutionException e) {
                System.out.println(e);
            }
            catch (TimeoutException | InterruptedException e) {
                System.out.println(e);
            }
        }
        return "It's over";
    }
}
