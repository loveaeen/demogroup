package com.itheima.mq.rocketmq.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Consumer1 {
	public static void main(String[] args) throws MQClientException {
		//1.创建消费者Consumer，制定消费者组名
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
		//2.指定Nameserver地址
		consumer.setNamesrvAddr("127.0.0.1:9876");
		//3.订阅主题Topic和Tag
		consumer.subscribe("OrderTopic", "*");
		//4.注册消息监听器, 最后控制台打印的结果你会发现, 相同订单的消息流一定是保序消费的, 但是不同订单的消息流之间不保证顺序消费的.
		// 注意, 这里传入的是 MessageListenerOrderly, 特点就是对一个队列采用的是单线程消费, 而不是 MessageListenerConcurrently（对一个队列中的消息采用多线程消费）
		// 所以你会发现同一组消息流是由同一根线程完成的. 不同消息流未必由同一根线程完成
		/* 最典型的打印结果如下
		 *  线程名称：【ConsumeMessageThread_1】:OrderStep{orderId=1065, desc='创建'}
			线程名称：【ConsumeMessageThread_1】:OrderStep{orderId=1065, desc='付款'}
			线程名称：【ConsumeMessageThread_1】:OrderStep{orderId=1065, desc='完成'}
			线程名称：【ConsumeMessageThread_2】:OrderStep{orderId=7235, desc='创建'}
			线程名称：【ConsumeMessageThread_2】:OrderStep{orderId=7235, desc='付款'}
			线程名称：【ConsumeMessageThread_2】:OrderStep{orderId=7235, desc='完成'}
			线程名称：【ConsumeMessageThread_3】:OrderStep{orderId=1039, desc='创建'}
			线程名称：【ConsumeMessageThread_3】:OrderStep{orderId=1039, desc='付款'}
			线程名称：【ConsumeMessageThread_3】:OrderStep{orderId=1039, desc='推送'}
			线程名称：【ConsumeMessageThread_3】:OrderStep{orderId=1039, desc='完成'}
		 */
		consumer.registerMessageListener(new MessageListenerOrderly() {

			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				for (MessageExt msg : msgs) {
					try {
						Random r = new Random();
						TimeUnit.MILLISECONDS.wait(r.nextInt(1000));
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					System.out.println(System.currentTimeMillis()+"  消费者1,当前消息组数量为:「"+msgs.size()+"」线程名称：【" + Thread.currentThread().getName() + "】:" + new String(msg.getBody()));
				}
				// context.setSuspendCurrentQueueTimeMillis(1000);
				// 返回消息消费的状态为成功
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});

		//5.启动消费者
		consumer.start();

		System.out.println("消费者1启动");

	}

//	public static void main(String[] args) throws ClientException, IOException, InterruptedException {
//        final ClientServiceProvider provider = ClientServiceProvider.loadService();
//        //接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8081;xxx:8081。
//        String endpoints = "localhost:8081";
//        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
//                .setEndpoints(endpoints)
//                .build();
//        //订阅消息的过滤规则，表示订阅所有Tag的消息。
//        String tag = "*";
//        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
//        //为消费者指定所属的消费者分组，Group需要提前创建。
//        String consumerGroup = "Your ConsumerGroup";
//        //指定需要订阅哪个目标Topic，Topic需要提前创建。
//        String topic = "TestTopic";
//        //初始化PushConsumer，需要绑定消费者分组ConsumerGroup、通信参数以及订阅关系。
//        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
//                .setClientConfiguration(clientConfiguration)
//                //设置消费者分组。
//                .setConsumerGroup(consumerGroup)
//                //设置预绑定的订阅关系。
//                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
//                //设置消费监听器。
//                .setMessageListener(messageView -> {
//                    //处理消息并返回消费结果。
//                    // LOGGER.info("Consume message={}", messageView);
//                    System.out.println("Consume1 message!! Tag is 「"+messageView.getTag()+"」，MessageGroup is 「"+messageView.getMessageGroup().get()+"」");
//                    return ConsumeResult.SUCCESS;
//                })
//                .build();
//        Thread.sleep(Long.MAX_VALUE);
//        //如果不需要再使用PushConsumer，可关闭该进程。
//        //pushConsumer.close();
//    }
}
