package com.chen.rabbitmqapi.consumer;
import com.chen.rabbitmqapi.uitl.ConnectionUtil;
import com.rabbitmq.client.Channel;

public class Consumer {
	public static void main(String[] args) throws Exception {
		Channel channel = ConnectionUtil.getChannel();
		String exchangeName = "test_consumer_exchange";
		String routingKey = "consumer.#";
		String queueName = "test_consumer_queue";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		channel.basicConsume(queueName, true, new MyConsumer(channel));
		
		
	}
}
