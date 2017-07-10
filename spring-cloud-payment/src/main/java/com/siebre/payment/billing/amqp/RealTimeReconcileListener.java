package com.siebre.payment.billing.amqp;

import com.siebre.payment.billing.base.ReconcileManager;
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

    @Autowired
    private ReconcileManager reconcileManager;

    @Override
    public void onMessage(Message message) {

        String orderNumber = new String(message.getBody());
        logger.info("实时对账队列接受到新的请求，开始去第三方查询订单状态，订单编号：{}", orderNumber);
        PaymentQueryResponse response = queryApplicationService.queryOrderStatusByOrderNumber(orderNumber);
        if(ReturnCode.SUCCESS.getDescription().equals(response.getReturnCode())) {
            reconcileManager.realTimeReconJob(response.getLocalOrder().getOrderNumber(), response.getQueryResult(), response.getRemoteJson());
        } else {
            logger.error("调用远程查询订单信息失败，无法开始实时对账，原因：{}", response.getReturnMessage());
            reconcileManager.createFailRealTimeReconJob(response.getLocalOrder().getOrderNumber(), response.getReturnMessage());
        }
    }
}
