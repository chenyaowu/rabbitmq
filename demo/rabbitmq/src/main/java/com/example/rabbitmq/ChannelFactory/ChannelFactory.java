package com.example.rabbitmq.ChannelFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelFactory {
    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private static Channel channel;
    private static ChannelFactory channelFactory;

    private ChannelFactory() throws IOException, TimeoutException {
        //1.创建连接工程
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("120.79.204.164");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //是否允许自动重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        //2.通过工厂创建连接
        connection = connectionFactory.newConnection();

        //3.通过连接创建channel
        channel = connection.createChannel();
    }

    public static ChannelFactory newInstance() throws IOException, TimeoutException {
        if (channelFactory == null) {
            channelFactory = new ChannelFactory();
        }
        return channelFactory;

    }

    public Channel getChannel() {
        return channel;
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
