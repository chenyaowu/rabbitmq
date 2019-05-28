package com.chen.rabbitmqapi.exchange.direct;

import com.chen.rabbitmqapi.uitl.ConnectionUtil;
import com.rabbitmq.client.Channel;


public class Producer4DirectExchange {

	
	public static void main(String[] args) throws Exception {

		Channel channel = ConnectionUtil.getChannel();
		//4 声明
		String exchangeName = "test_direct_exchange";
		String routingKey = "test.direct";
		//5 发送
		
		String msg = "Hello World RabbitMQ 4  Direct Exchange Message 111 ... ";
		channel.basicPublish(exchangeName, routingKey , null , msg.getBytes()); 		

		ConnectionUtil.close();
	}
	
}
