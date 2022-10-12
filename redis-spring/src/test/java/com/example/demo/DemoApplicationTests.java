package com.example.demo;

import com.example.demo.pojo.SysUser;
import com.example.demo.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;
    @Test
    void contextLoads() throws JsonProcessingException, InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            redisTemplate.opsForValue().get(String.valueOf(i));
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }



}
class CountTedInteger{
    private static long counter = 0;
    private final long id  = counter++;

    @Override
    public String toString() {
        return Long.toString(id);
    }
}