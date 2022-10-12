package com.itheima.mq.rocketmq.base.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * 发送同步消息
 */
public class SyncProducer {

	public static void main(String[] args) throws Exception {
		//1.创建消息生产者producer，并制定生产者组名
		DefaultMQProducer producer = new DefaultMQProducer("group1");
		producer.setVipChannelEnabled(false);
		//2.指定Nameserver地址
//		producer.setNamesrvAddr("127.0.0.1:9876");
		producer.setNamesrvAddr("172.16.0.49:9876;172.16.0.48:9876");
		//3.启动producer
		producer.start();

		for (int i = 0; i < 10; i++) {
			//4.创建消息对象，指定主题Topic、Tag和消息体
			/**
			 * 参数一：消息主题Topic
			 * 参数二：消息Tag
			 * 参数三：消息内容
			 * 当然, Message 的其他构造函数还可以指定消息的键(key)
			 */
			Message msg = new Message("springboot-mq", "Tag1", ("Hello World" + i).getBytes());
			//5.同步发送消息
			SendResult result = producer.send(msg);
			//发送状态
			SendStatus status = result.getSendStatus();

			System.out.println("发送结果: " + status);

			// 消息 id
			String msgId = result.getMsgId();

			System.out.println("消息id: " + msgId);

			// 消息接收队列id
			Integer messageQueueId = result.getMessageQueue().getQueueId();

			System.out.println("消息分区ID: " + messageQueueId);

			// 最后打印总的结果
			// SendResult [sendStatus=SEND_OK, msgId=AC10644F07F8442D9B6E5ECB6F760000, offsetMsgId=C0A83E0300002A9F0000000000058458, messageQueue=MessageQueue [topic=springboot-mq, brokerName=broker-a, queueId=1], queueOffset=2]
			// 注意, broker-name 是你把消息发送到了哪个主从对的主去了(因为只能主节点进行写入)
			System.out.println(result);

			//线程睡1秒
			TimeUnit.SECONDS.sleep(1);
		}

		//6.关闭生产者producer
		producer.shutdown();
	}
}