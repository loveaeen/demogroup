package com.example.kafka.example.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("topic1", "" + i, "test-----" + i);
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

    @GetMapping("/sync")
    public String sync() {
        for (int i = 0; i < 100; i++) {
            try {
                kafkaTemplate.send("topic1", "" + i, "test-----" + i).get(5, TimeUnit.SECONDS);
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
