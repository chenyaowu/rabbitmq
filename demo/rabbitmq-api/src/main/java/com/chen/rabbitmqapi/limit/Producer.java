package com.chen.rabbitmqapi.limit;

import com.chen.rabbitmqapi.uitl.ConnectionUtil;
import com.rabbitmq.client.Channel;

public class Producer {

	
	public static void main(String[] args) throws Exception {

		Channel channel = ConnectionUtil.getChannel();
		
		String exchange = "test_qos_exchange";
		String routingKey = "qos.save";
		
		String msg = "Hello RabbitMQ QOS Message";
		
		for(int i =0; i<5; i ++){
			channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
		}

		ConnectionUtil.close();
		
	}
}
