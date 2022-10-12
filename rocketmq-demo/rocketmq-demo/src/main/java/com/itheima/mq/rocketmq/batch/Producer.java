package com.itheima.mq.rocketmq.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Producer {

	public static void main(String[] args) throws Exception {
		//1.创建消息生产者producer，并制定生产者组名
		DefaultMQProducer producer = new DefaultMQProducer("group1");
		//2.指定Nameserver地址
		producer.setNamesrvAddr("127.0.0.1:9876");
		//3.启动producer
		producer.start();

		List<Message> msgs = new ArrayList<>();

		//4.创建消息对象，指定主题Topic、Tag和消息体
		/**
		 * 参数一：消息主题Topic
		 * 参数二：消息Tag
		 * 参数三：消息内容
		 */
		Message msg1 = new Message("BatchTopic", "Tag1", ("Hello World" + 1).getBytes());
		Message msg2 = new Message("BatchTopic", "Tag1", ("Hello World" + 2).getBytes());
		Message msg3 = new Message("BatchTopic", "Tag1", ("Hello World" + 3).getBytes());

		msgs.add(msg1);
		msgs.add(msg2);
		msgs.add(msg3);

		//5.发送消息
		SendResult result = producer.send(msgs);
		//发送状态
		SendStatus status = result.getSendStatus();

		// 发送结果:SendResult [sendStatus=SEND_OK, msgId=AC10644F34B4442D9B6E5F3D693A0000,AC10644F34B4442D9B6E5F3D693A0001,AC10644F34B4442D9B6E5F3D693A0002, offsetMsgId=C0A83E0300002A9F000000000005BEC1,C0A83E0300002A9F000000000005BF70,C0A83E0300002A9F000000000005C01F, messageQueue=MessageQueue [topic=BatchTopic, brokerName=broker-a, queueId=1], queueOffset=0]
		System.out.println("发送结果:" + result);

		//线程睡1秒
		TimeUnit.SECONDS.sleep(1);

		//6.关闭生产者producer
		producer.shutdown();
	}

}
