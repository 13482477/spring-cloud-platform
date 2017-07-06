package com.siebre.payment.billing.amqp;

import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.service.queryapplication.QueryApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Huang Tianci
 * 实时对账监听器
 */
public class RealTimeReconcileListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(RealTimeReconcileListener.class);

    @Autowired
    private QueryApplicationService queryApplicationService;

    @Override
    public void onMessage(Message message) {
        String orderNumber = new String(message.getBody());

        logger.info("开始执行实时对账，订单编号：{}", orderNumber);
        //TODO 实时对账
        PaymentQueryResponse response = queryApplicationService.queryOrderStatusByOrderNumber(orderNumber);
        if(ReturnCode.SUCCESS.equals(response.getReturnCode())) {

        } else {
            logger.error("实时对账失败，原因：{}", response.getReturnMessage());
        }
    }
}
