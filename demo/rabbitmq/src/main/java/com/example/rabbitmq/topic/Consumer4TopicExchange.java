package com.example.rabbitmq.topic;

import com.example.rabbitmq.ChannelFactory.ChannelFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer4TopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ChannelFactory channelFactory = ChannelFactory.newInstance();
        Channel channel = channelFactory.getChannel();

        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_name";
        String routingKey = "user.#";
        //声明一个交换机
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
       // 4.声明（创建）一个队列
        channel.queueDeclare(queueName,true,false,false,null);
        //建立一个绑定关系
        channel.queueBind(queueName,exchangeName,routingKey);

        //5.创建一个消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6.设置channel
        channel.basicConsume(queueName,true,queueingConsumer);

        //7.获取消息
        while (true){
            QueueingConsumer.Delivery delivery= queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费端："+msg);
        }
    }
}
