package com.example.rabbitmq.topic;

import com.example.rabbitmq.ChannelFactory.ChannelFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer4TopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException {
        ChannelFactory channelFactory = ChannelFactory.newInstance();
        Channel channel = channelFactory.getChannel();

        //4.声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.save";
        String routingKey2 = "user.update";
        String routingKey3 = "user.delete.abc";

        //5.通过channel发送数据
        String msg = "hello rabbitmq 5 topic exchange message";
        channel.basicPublish(exchangeName, routingKey1, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey2, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey3, null, msg.getBytes());


        channelFactory.close();


    }
}
