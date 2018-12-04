package com.example.rabbitmq.api.message;

import com.example.rabbitmq.ChannelFactory.ChannelFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ChannelFactory channelFactory = ChannelFactory.newInstance();
        Channel channel = channelFactory.getChannel();

        //4.声明（创建）一个队列
        String queueName = "test001";
        channel.queueDeclare(queueName,true,false,false,null);


        //5.创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6.设置channel
        channel.basicConsume(queueName,true,queueingConsumer);

        while (true){
            //7.获取消息
            QueueingConsumer.Delivery delivery= queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            Map<String,Object> map =  delivery.getProperties().getHeaders();

        }
    }
}
