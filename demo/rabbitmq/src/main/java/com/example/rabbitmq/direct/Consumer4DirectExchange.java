package com.example.rabbitmq.direct;

import com.example.rabbitmq.ChannelFactory.ChannelFactory;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer4DirectExchange {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ChannelFactory channelFactory = ChannelFactory.newInstance();
        Channel channel = channelFactory.getChannel();

        String exchangeName = "test_direct_exchange";
        String exchangeType = "direct";
        String queueName = "test_direct_name";
        String routingKey = "test.direct";
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
