package com.itheima.mq.rocketmq.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer2 {
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
					System.out.println("消费者2,线程名称：【" + Thread.currentThread().getName() + "】:" + new String(msg.getBody()));
				}
				// context.setSuspendCurrentQueueTimeMillis(1000);
				// 返回消息消费的状态为成功
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});

		//5.启动消费者
		consumer.start();

		System.out.println("消费者2启动");

	}
}
