package com.chen.rabbitmqapi.exchange.fanout;

import com.chen.rabbitmqapi.uitl.ConnectionUtil;
import com.rabbitmq.client.Channel;

public class Producer4FanoutExchange {


    public static void main(String[] args) throws Exception {
        Channel channel = ConnectionUtil.getChannel();
        //4 声明
        String exchangeName = "test_fanout_exchange";
        //5 发送
        for (int i = 0; i < 10; i++) {
            String msg = "Hello World RabbitMQ 4 FANOUT Exchange Message ...";
            channel.basicPublish(exchangeName, "", null, msg.getBytes());
        }
        ConnectionUtil.close();

    }

}
