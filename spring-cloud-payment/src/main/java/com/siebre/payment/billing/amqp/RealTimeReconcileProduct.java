package com.siebre.payment.billing.amqp;

import com.siebre.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Huang Tianci
 *         实时对账服务
 */
@Service
public class RealTimeReconcileProduct {

    private Logger logger = LoggerFactory.getLogger(RealTimeReconcileProduct.class);

    @Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    public void sendToRealTimeExchange(String orderNumber) {
        logger.info("发送到实时对账Exchange, 订单编号为：{}", orderNumber);
        rabbitTemplate.convertAndSend(RabbitmqConfig.order_real_time_recon_exchange, RabbitmqConfig.order_real_time_recon_key, orderNumber);
    }

}
