package com.example.rabbitmq.fanout;

import com.example.rabbitmq.ChannelFactory.ChannelFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer4FanoutExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        ChannelFactory channelFactory = ChannelFactory.newInstance();
        Channel channel = channelFactory.getChannel();

        //4.声明
        String exchangeName = "test_fanout_exchange";

        //5.发送
        for (int i = 0; i < 10; i++) {
            String msg = "Hello World RabbitMQ 4 FANOUT Exchange Message..";
            channel.basicPublish(exchangeName,"",null,msg.getBytes());
        }
        channel.close();

    }
}
