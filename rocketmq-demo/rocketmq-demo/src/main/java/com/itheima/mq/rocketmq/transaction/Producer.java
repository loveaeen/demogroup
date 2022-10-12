package com.itheima.mq.rocketmq.transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.TimeUnit;

/**
 * 发送同步消息
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		//1.创建消息生产者producer，并制定生产者组名
		TransactionMQProducer producer = new TransactionMQProducer("group5");
		//2.指定Nameserver地址
		producer.setNamesrvAddr("127.0.0.1:9876");

		//添加事务监听器
		producer.setTransactionListener(new TransactionListener() {
			/**
			 * 在该方法中执行本地事务
			 * @param msg
			 * @param arg
			 * @return
			 */
			@Override
			public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
				if (StringUtils.equals("TAGA", msg.getTags())) {
					// 如果是TAGA 消息, 则通知broker 进行消息的提交
					return LocalTransactionState.COMMIT_MESSAGE;
				} else if (StringUtils.equals("TAGB", msg.getTags())) {
					// 如果是TAGB 消息, 则通知 broker 进行消息的回滚, 所以消费端只能消费到两条消息
					return LocalTransactionState.ROLLBACK_MESSAGE;
				} else if (StringUtils.equals("TAGC", msg.getTags())) {
					// 如果是 TAGC 消息, 则通知 broker 过来进行事务回查
					return LocalTransactionState.UNKNOW;
				} else {
					return LocalTransactionState.UNKNOW;
				}
			}

			/**
			 * 该方法时MQ进行消息事务状态回查
			 * @param msg
			 * @return
			 */
			@Override
			public LocalTransactionState checkLocalTransaction(MessageExt msg) {
				System.out.println("消息的Tag:" + msg.getTags());
				// 这里会告诉 broker, TAGC 的消息也可以让消费者消费
				return LocalTransactionState.COMMIT_MESSAGE;
			}
		});

		//3.启动producer
		producer.start();

		String[] tags = { "TAGA", "TAGB", "TAGC" };

		// 发送三条消息, 对不同的消息给出不同的处理方式
		for (int i = 0; i < 3; i++) {
			//4.创建消息对象，指定主题Topic、Tag和消息体
			/**
			 * 参数一：消息主题Topic
			 * 参数二：消息Tag
			 * 参数三：消息内容
			 */
			Message msg = new Message("TransactionTopic", tags[i], ("Hello World" + i).getBytes());
			//5.发送消息, 第二个 null 表示对发送的全部三条消息都进行事务控制
			SendResult result = producer.sendMessageInTransaction(msg, null);
			//发送状态
			SendStatus status = result.getSendStatus();

			System.out.println("发送结果:" + result);

			//线程睡1秒
			TimeUnit.SECONDS.sleep(2);
		}

		//6.关闭生产者producer, 这里不能关闭, 不然broker无法回查事务执行状态
		//producer.shutdown();
	}
}
