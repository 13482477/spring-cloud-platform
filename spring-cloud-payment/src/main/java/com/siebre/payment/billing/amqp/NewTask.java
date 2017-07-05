package com.siebre.payment.billing.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.concurrent.TimeoutException;

/**
 * Created by tianci.huang on 2017/7/5.
 */
public class NewTask {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] argv)
            throws java.io.IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.18.223");
        factory.setPort(5672);
        factory.setUsername("mq");
        factory.setPassword("mq");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //将queue declare durable
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        String message = "hello .....34..";

        //将message declare persistence use by MessageProperties.PERSISTENT_TEXT_PLAIN
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

}
