package com.example.zookeeper;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ZookeeperApplicationTests {

    @Test
    void contextLoads() throws Exception {
        ZKCustor zkCustor = new ZKCustor();
        zkCustor.init();
        zkCustor.watchNode("/1521");
        zkCustor.createNode("/1521");
        TimeUnit.HOURS.sleep(1);
    }

    @Test
    void test(){
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:oracle:thin:@192.168.1.180:1521:orcl");
        ds.setUsername("jdzfgl_liaon");
        ds.setPassword("hfits_kfkpass");
        ds.setInitialSize(20);
        ds.setMaxActive(50);
        ds.setValidationQuery("SELECT COUNT(*) FROM DUAL");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        //jdbcTemplate.execute("insert into trans_demo values(1,'张三')");
        test2(jdbcTemplate);
    }

    @Transactional
    void test2(JdbcTemplate jdbcTemplate){
        jdbcTemplate.execute("insert into trans_demo values(2,'李四')");
        jdbcTemplate.execute("insert into trans_demo values(3,'王五')");
        throw new RuntimeException();
    }
}
