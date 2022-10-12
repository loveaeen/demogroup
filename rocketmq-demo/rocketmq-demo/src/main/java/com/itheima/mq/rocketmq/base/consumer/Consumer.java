package com.itheima.mq.rocketmq.base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 消息的接受者
 */
public class Consumer {

	public static void main(String[] args) throws Exception {
		//1.创建消费者Consumer, 指定消费者组名, 这里使用的是 Push 模式下的消费者, 即broker 主动向我们消费者推送消息
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
		//2.指定Nameserver地址
		consumer.setNamesrvAddr("127.0.0.1:9876");
		//3.订阅主题Topic和Tag
		// 消费 springboot-mq 主题中的 任意 tag 的消息
		consumer.subscribe("springboot-mq", "*");
		// 消费 springboot-mq 主题中tag为的 tag1 的消息
		//		consumer.subscribe("springboot-mq", "tag1");
		// 消费 springboot-mq 主题中 tag 为 tag1 或者 tag2 的消息
		//		consumer.subscribe("springboot-mq", "tag1 || tag2");

		//设定消费模式：负载均衡(这是默认的消息消费的方式)
		consumer.setMessageModel(MessageModel.CLUSTERING);
		//设定消费模式：广播模式
		//		consumer.setMessageModel(MessageModel.BROADCASTING);

		//4.设置回调函数，处理消息
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			//接受消息的回调
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				// 打印收到的消息
				for (MessageExt msg : msgs) {
					System.out.println(
							"consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
				}
				// 返回消息消费的结果, 这里告诉消息消费成功
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		//5.启动消费者consumer, 则消费者线程会一直停在这里
		consumer.start();
	}
}
