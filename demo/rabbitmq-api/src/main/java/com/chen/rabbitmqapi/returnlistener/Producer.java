package com.chen.rabbitmqapi.returnlistener;

import java.io.IOException;

import com.chen.rabbitmqapi.uitl.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Producer {

	
	public static void main(String[] args) throws Exception {

		Channel channel = ConnectionUtil.getChannel();
		String exchange = "test_return_exchange";
		String routingKey = "return.save";
		String routingKeyError = "abc.save";
		
		String msg = "Hello RabbitMQ Return Message";
		
		
		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange,
				String routingKey, BasicProperties properties, byte[] body) throws IOException {
				
				System.out.println("---------handle  return----------");
				System.out.println("replyCode: " + replyCode);
				System.out.println("replyText: " + replyText);
				System.out.println("exchange: " + exchange);
				System.out.println("routingKey: " + routingKey);
				System.out.println("properties: " + properties);
				System.out.println("body: " + new String(body));
			}
		});
		
		
		//channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
		
		channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

	}
}
