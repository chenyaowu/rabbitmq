package com.example.rabbitmq.direct;

import com.example.rabbitmq.ChannelFactory.ChannelFactory;
import com.rabbitmq.client.Channel;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer4DirectExchange {
    public static void main(String[] args) throws IOException, TimeoutException {
        ChannelFactory channelFactory = ChannelFactory.newInstance();
        Channel channel = channelFactory.getChannel();


        //4.声明
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";


        //5.通过channel发送数据
        String msg = "hello rabbitmq 5 direct exchange message";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
        }
        channelFactory.close();


    }
}
