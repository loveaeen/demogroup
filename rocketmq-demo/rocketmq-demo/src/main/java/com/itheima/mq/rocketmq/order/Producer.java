package com.itheima.mq.rocketmq.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

public class Producer {

	// 4.4.0
	public static void main(String[] args) throws Exception {
		//1.创建消息生产者producer，并制定生产者组名
		DefaultMQProducer producer = new DefaultMQProducer("group1");
		//2.指定Nameserver地址
		producer.setNamesrvAddr("127.0.0.1:9876");
		//3.启动producer
		producer.start();
		//4.设定有几个queue 默认是4个
		// producer.setDefaultTopicQueueNums(1);

		//构建消息集合
		List<OrderStep> orderSteps = OrderStep.buildOrders();
		List<OrderStep> orderSteps1 = OrderStep.buildOrders();
		orderSteps.addAll(orderSteps1);
		//发送消息
		for (int i = 0; i < 100; i++) {
			// 参数1: topic、参数2: tag、参数3: key、参数4: 消息体
			Message message = new Message("OrderTopic", "Order", "i" + i, ("test "+ i).getBytes());
			/**
			 * 参数一：消息对象
			 * 参数二：消息队列的选择器, 以实现某组消息固定选择一个queue去推送消息.
			 * 参数三：选择队列的业务标识（订单ID）
			 */
			SendResult sendResult = producer.send(message, new MessageQueueSelector() {
				/**
				 *
				 * @param mqs：队列集合
				 * @param msg：消息对象
				 * @param arg：业务标识的参数, 由上面的`参数三` 决定
				 * @return
				 */
				@Override
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					long orderId = (long) arg;
					long index = orderId % mqs.size();
					return mqs.get((int) index);
				}
			}, 3L);

			System.out.println("发送结果：" + sendResult);
		}
		producer.shutdown();
	}

	// 5.0.0
//	public static void main(String[] args) throws ClientException {
//		//接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8081;xxx:8081。
//        String endpoint = "localhost:8081";
//        //消息发送的目标Topic名称，需要提前创建。
//        String topic = "TestTopic";
//        ClientServiceProvider provider = ClientServiceProvider.loadService();
//        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoint);
//        ClientConfiguration configuration = builder.build();
//        //初始化Producer时需要设置通信配置以及预绑定的Topic。
//		org.apache.rocketmq.client.apis.producer.Producer producer = provider.newProducerBuilder()
//				.setTopics(topic)
//				.setClientConfiguration(configuration)
//				.build();
//		String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
//		for (int i = 0; i < 100; i++) {
//			Message message = provider.newMessageBuilder().setTopic(topic)
//                //设置消息索引键，可根据关键字精确查找某条消息。
//                .setKeys("KEY"+i)
//                //设置消息Tag，用于消费端根据指定Tag过滤消息。
//                .setTag(tags[i % tags.length])
//                //设置顺序消息的排序分组，该分组尽量保持离散，避免热点排序分组。
//                .setMessageGroup("fifoGroup001")
//                //消息体。
//                .setBody("messageBody".getBytes())
//                .build();
//			try {
//				//发送消息，需要关注发送结果，并捕获失败等异常
//				SendReceipt sendReceipt = producer.send(message);
//				System.out.println(sendReceipt.getMessageId());
//			} catch (ClientException e) {
//				e.printStackTrace();
//			}
//		}
//	}

}
