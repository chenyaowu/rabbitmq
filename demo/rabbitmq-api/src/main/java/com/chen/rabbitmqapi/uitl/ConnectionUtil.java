package com.chen.rabbitmqapi.uitl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ConnectionUtil {

    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private static Channel channel;

    static {
        //1 创建一个ConnectionFactory, 并进行配置
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.6");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        try {
            connection = connectionFactory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public static Connection getConnection() {
        //2 通过连接工厂创建连接
        return connection;
    }

    public static Channel getChannel() {
        return channel;
    }

    public static void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
