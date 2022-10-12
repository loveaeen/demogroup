package com.itheima.mq.rocketmq.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单构建者
 */
public class OrderStep {
	private long orderId;
	private String desc;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "OrderStep{" + "orderId=" + orderId + ", desc='" + desc + '\'' + '}';
	}

	public static List<OrderStep> buildOrders() {
		// 有三个业务消息队列, 即三个订单
		//  1039L   : 创建    付款 推送 完成
		//  1065L   ： 创建   付款
		//  7235L   ：创建    付款
		List<OrderStep> orderList = new ArrayList<OrderStep>();

		OrderStep orderDemo = new OrderStep();
		orderDemo.setOrderId(1039L);
		orderDemo.setDesc("创建1");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1039L);
		orderDemo.setDesc("创建2");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1039L);
		orderDemo.setDesc("创建3");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1045L);
		orderDemo.setDesc("付款1");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1045L);
		orderDemo.setDesc("付款2");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1045L);
		orderDemo.setDesc("付款3");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1065L);
		orderDemo.setDesc("完成1");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1065L);
		orderDemo.setDesc("完成2");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1065L);
		orderDemo.setDesc("完成3");
		orderList.add(orderDemo);

		orderDemo = new OrderStep();
		orderDemo.setOrderId(1089L);
		orderDemo.setDesc("推送");
		orderList.add(orderDemo);

		return orderList;
	}
}
