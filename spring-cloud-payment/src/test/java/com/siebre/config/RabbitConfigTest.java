package com.siebre.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by AdamTang on 2017/4/11.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/applicationContext-rabbit.xml",
        "classpath:spring/applicationContext-bean.xml",
        "classpath:spring/applicationContext-jdbc.xml",
})
public class RabbitConfigTest {

    @Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate template;

    @Test
    public void test() {
        final long start = System.currentTimeMillis();


        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        template.convertAndSend("orderOutOfTimeExchange", "orderOutOfTimeKey", uuid+";"+System.currentTimeMillis(),
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setDelay(1000 * 10);
                        return message;
                    }
                });

            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
}
