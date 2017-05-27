package com.siebre.payment.service.paymentlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.rabbitmq.client.Channel;
import com.siebre.payment.service.paymenttransaction.PaymentTransactionService;

/**
 * Created by AdamTang on 2017/4/11.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 采用rabbitMQ ACK机制
 * 被提取过但是未被确认的消息的状态被重置,在connection释放连接之后 它就可以被重新提取.
 * 应用场景：订单超时的更新服务数据库异常，所以订单无法被设置为超时，需要排除异常信息之后
 * 重新把超时的订单同步状态到数据库
 */
public class PaymentOrderOutOfTimeListener implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(PaymentOrderOutOfTimeListener.class);

    @Autowired
    private PaymentTransactionService transactionService;

    @Override
    @Transactional("mq")
    public void onMessage(Message message, Channel channel) throws Exception {

        try {
            String orderNumber = new String(message.getBody());

            transactionService.outOfTime(orderNumber);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
        }catch (Exception e){
            //异常不对消息进行确认
            logger.error("订单超时处理失败",e);
        }
    }
}
