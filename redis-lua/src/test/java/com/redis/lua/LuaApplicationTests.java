package com.redis.lua;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class LuaApplicationTests {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public static ExecutorService executorService = Executors.newFixedThreadPool(30);

    @Test
    void contextLoads() throws InterruptedException {

        String key = "kc";
        redisTemplate.opsForValue().set(key,1000);
        String subStock="local key=KEYS[1];\n" +
                "local subNum = tonumber(ARGV[1]) ;\n" +
                "local surplusStock = tonumber(redis.call('get',key));\n" +
                "if (surplusStock<=0) then return 0\n" +
                "elseif (subNum > surplusStock) then  return 1\n" +
                "else\n" +
                "    redis.call('incrby', KEYS[1], -subNum)\n" +
                "    return 2 \n" +
                "end";
        DefaultRedisScript<Long> longDefaultRedisScript = new DefaultRedisScript<>(subStock, long.class);
        for (int i = 0; i < 1500; i++) {
            executorService.execute(()->{
                Long execute = redisTemplate.execute(longDefaultRedisScript, Arrays.asList(key), 1);
                if(execute == 2){
                    System.out.println("成功抢到啦");
                }else if(execute == 0){
                    System.out.println("库存不足了！");
                }else if(execute == 1){
                    System.out.println("你抢的数量太多了，库存不够！");
                }
            });
        }
        TimeUnit.HOURS.sleep(1);
    }

}
