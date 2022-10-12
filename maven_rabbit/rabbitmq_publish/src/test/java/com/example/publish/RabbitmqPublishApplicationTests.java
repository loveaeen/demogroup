package com.example.publish;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class RabbitmqPublishApplicationTests {

    @Test
    void contextLoads() throws IOException, TimeoutException {

    }

    @Test
    public static void main(String[] args) throws IOException, TimeoutException {
        routing();
    }


    static void work() throws IOException, TimeoutException {
        //创建mq的连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接的主机
        connectionFactory.setHost("121.40.88.133");
        //设置连接的端口
        connectionFactory.setPort(5672);
        //设置连接哪个主机
        connectionFactory.setVirtualHost("/");
        //设置账户密码
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //获取连接对象
        Connection connection = connectionFactory.newConnection();
        //获取连接中的channel频道
        Channel channel = connection.createChannel();



        //通道绑定对应消息队列
        //参数1: 队列名称如果队列不存在自动创建
        //参数2: 用来定义队列特性是否要持久化true持久化队列 false不持久化(消息不持久)
        //参数3: exclusive是否独占队列 true独占队列 false 不独占
        //参数4: autoDelete:是否在消费完成后自动删除队列 true自动删除 false不自动删除
        //参数5: 附加参数
        channel.queueDeclare("rabbit",false,false,false,null);

        //发布消息
        //参数1:交换机名称
        //参数2:队列名称
        //参数3:传递消息额外设置
        //参数4:消息的具体内容
        for (int i = 0; i < 100; i++) {
            channel.basicPublish("","rabbit",null,("work rabbitmq"+i).getBytes());
        }

        //channel.close();
        //connection.close();
    }


    static void fanout() throws IOException, TimeoutException {
        //创建mq的连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接的主机
        connectionFactory.setHost("121.40.88.133");
        //设置连接的端口
        connectionFactory.setPort(5672);
        //设置连接哪个主机
        connectionFactory.setVirtualHost("/rabbit");
        //设置账户密码
        connectionFactory.setUsername("handana");
        connectionFactory.setPassword("han4318233");

        //获取连接对象
        Connection connection = connectionFactory.newConnection();
        //获取连接中的channel频道
        Channel channel = connection.createChannel();

        //交换机
        //参数2:fanout 广播类型
        channel.exchangeDeclare("logs","fanout");

        //通道绑定对应消息队列
        //参数1: 队列名称如果队列不存在自动创建
        //参数2: 用来定义队列特性是否要持久化true持久化队列 false不持久化(消息不持久)
        //参数3: exclusive是否独占队列 true独占队列 false 不独占
        //参数4: autoDelete:是否在消费完成后自动删除队列 true自动删除 false不自动删除
        //参数5: 附加参数
        //channel.queueDeclare("rabbit",false,false,false,null);

        //发布消息
        //参数1:交换机名称
        //参数2:队列名称
        //参数3:传递消息额外设置
        //参数4:消息的具体内容
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("logs","",null,("fanout rabbitmq"+i).getBytes());
        }

        channel.close();
        connection.close();
    }

    static void routing() throws IOException, TimeoutException {
        //创建mq的连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接的主机
        connectionFactory.setHost("121.40.88.133");
        //设置连接的端口
        connectionFactory.setPort(5672);
        //设置连接哪个主机
        connectionFactory.setVirtualHost("/");
        //设置账户密码
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //获取连接对象
        Connection connection = connectionFactory.newConnection();
        //获取连接中的channel频道
        Channel channel = connection.createChannel();
        //交换机
        channel.exchangeDeclare("logs_direct","direct");
        //channel.queueDeclare("jiedu",false,false,false,null);

        String routingKey = "info";
        String routingKeyTemp = "error";
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("logs_direct",routingKey,null,("direct rabbitmq info"+i).getBytes());
            if(i > 5)
                channel.basicPublish("logs_direct",routingKeyTemp,null,("direct rabbitmq error"+i).getBytes());
        }

        channel.close();
        connection.close();

    }

    static void topic() throws IOException, TimeoutException {
        //创建mq的连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接的主机
        connectionFactory.setHost("121.40.88.133");
        //设置连接的端口
        connectionFactory.setPort(5672);
        //设置连接哪个主机
        connectionFactory.setVirtualHost("/rabbit");
        //设置账户密码
        connectionFactory.setUsername("handana");
        connectionFactory.setPassword("han4318233");

        //获取连接对象
        Connection connection = connectionFactory.newConnection();
        //获取连接中的channel频道
        Channel channel = connection.createChannel();
        //交换机
        channel.exchangeDeclare("logs_topic","topic");

        String routingKey = "user.info";
        String routingKeyTemp = "role.info";
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("logs_topic",routingKey,null,("topic rabbitmq user"+i).getBytes());
            if(i > 5)
                channel.basicPublish("logs_topic",routingKeyTemp,null,("topic rabbitmq role"+i).getBytes());
        }

        channel.close();
        connection.close();

    }

}
