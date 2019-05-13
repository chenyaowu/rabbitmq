package com.example.rabbitmq.quickstart;

import com.example.rabbitmq.ChannelFactory.ChannelFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Procuder {
    public static void main(String[] args) throws IOException, TimeoutException {
        ChannelFactory channelFactory = ChannelFactory.newInstance();
        Channel channel = channelFactory.getChannel();


        //4.通过channel发送数据
        String msg = "hello rabbitmq";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("","test001",null,msg.getBytes());
        }
        channelFactory.close();




    }
}
