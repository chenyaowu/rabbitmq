package com.chen.rabbitmqapi.confirm;

import java.io.IOException;

import com.chen.rabbitmqapi.uitl.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;

public class Producer {
	public static void main(String[] args) throws Exception {

		Channel channel = ConnectionUtil.getChannel();

		//4 指定我们的消息投递模式: 消息的确认模式 
		channel.confirmSelect();
		
		String exchangeName = "test_confirm_exchange";
		String routingKey = "confirm.save";
		
		//5 发送一条消息
		String msg = "Hello RabbitMQ Send confirm message!";
		channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
		
		//6 添加一个确认监听
		channel.addConfirmListener(new ConfirmListener() {
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("-------no ack!-----------");
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("-------ack!-----------");
			}
		});
	}
}
