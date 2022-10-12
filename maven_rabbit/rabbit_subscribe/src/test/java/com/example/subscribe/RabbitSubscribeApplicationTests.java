package com.example.subscribe;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.LongStream;

@SpringBootTest
class RabbitSubscribeApplicationTests {

    @Test
    void contextLoads() throws IOException, TimeoutException {

    }

    @Test
    public static void main(String[] args) throws IOException, TimeoutException {
        routing();
        //long reduce = LongStream.rangeClosed(0L, 5).map(n->n*n).reduce(0L,Long::sum);
        //System.out.println(reduce);
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


        //绑定队列
        channel.queueDeclare("rabbit",false,false,false,null);

        //消息消息
        //参数1:消费哪个队列
        //参数2:开启消息的自动确认机制
        //参数3:消息消费时的回调接口
        channel.basicConsume("rabbit",true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("1号消费者消息收到了----->"+new String(body));
            }
        });
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

        //绑定队列
        //channel.queueDeclare("rabbit",false,false,false,null);
        //临时队列
        String queueName = channel.queueDeclare().getQueue();

        //绑定队列到交换机
        channel.queueBind(queueName,"logs","");

        //消息消息
        //参数1:消费哪个队列
        //参数2:开启消息的自动确认机制
        //参数3:消息消费时的回调接口
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("1号消费者消息收到了----->"+new String(body));
            }
        });
        //channel.close();
        //connection.close();
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

        //临时队列
        String queueName = channel.queueDeclare().getQueue();

        //绑定队列到交换机
        channel.queueBind(queueName,"logs_direct","info");
        channel.queueBind(queueName,"logs_direct","error");

        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("1号消费者消息收到了----->"+new String(body));
            }
        });

        //channel.close();
        //connection.close();

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

        //临时队列
        String queueName = channel.queueDeclare().getQueue();

        //绑定队列到交换机
        channel.queueBind(queueName,"logs_topic","user.*");

        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("1号消费者消息收到了----->"+new String(body));
            }
        });

        //channel.close();
        //connection.close();

    }

}
