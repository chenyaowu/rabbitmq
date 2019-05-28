package com.chen.rabbitmqapi.quickstart;

import com.chen.rabbitmqapi.uitl.ConnectionUtil;
import com.rabbitmq.client.Channel;

public class Procuder {


    public static void main(String[] args) throws Exception {

        Channel channel = ConnectionUtil.getChannel();

        //4 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ!";
            //1 exchange   2 routingKey
            channel.basicPublish("", "test001", null, msg.getBytes());
        }

        //5 记得要关闭相关的连接
        ConnectionUtil.close();
    }
}
