package com.siebre.payment.paymentlistener;

import com.siebre.config.RabbitmqConfig;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by AdamTang on 2017/4/11.
 * Project:siebre-cloud-platform
 * Version:1.0
 * applicationContext-rabbit.xml
 */
@Service
public class PaymentOrderOutOfTimeService {

    public static final int OUT_OF_TIME = 1000 * 60 * 30;//默认30分钟

    @Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    public void newOrder(PaymentOrder order) {

        rabbitTemplate.convertAndSend(RabbitmqConfig.order_out_of_time_exchange, RabbitmqConfig.order_out_of_time_key, order.getOrderNumber(),
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        //设置订单的超时时间
                        message.getMessageProperties().setDelay(OUT_OF_TIME);
                        return message;
                    }
                });
    }

}
