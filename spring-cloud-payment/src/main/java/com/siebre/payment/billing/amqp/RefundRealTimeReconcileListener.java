package com.siebre.payment.billing.amqp;

import com.siebre.payment.billing.base.ReconcileManager;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymenthandler.paymentquery.PaymentQueryResponse;
import com.siebre.payment.service.queryapplication.QueryApplicationService;
import com.siebre.payment.service.queryapplication.RefundQueryApplicationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Huang Tianci
 * 退款实时对账监听器
 */
public class RefundRealTimeReconcileListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(RefundRealTimeReconcileListener.class);

    @Autowired
    private RefundQueryApplicationService refundQueryApplicationService;

    @Autowired
    private ReconcileManager reconcileManager;

    @Override
    public void onMessage(Message message) {

        String orderNumber = new String(message.getBody());
        if(StringUtils.isBlank(orderNumber)) {
            logger.info("订单编号为空，忽略：{}", orderNumber);
            return;
        }
        logger.info("退款实时对账队列接受到新的请求，开始去第三方查询订单退款状态，订单编号：{}", orderNumber);
        PaymentQueryResponse response = refundQueryApplicationService.queryOrderRefundStatusByOrderNumber(orderNumber);
        if(ReturnCode.SUCCESS.getDescription().equals(response.getReturnCode())) {
            reconcileManager.refundRealTimeReconJob(response.getLocalOrder().getOrderNumber(), response.getQueryResult(), response.getRemoteJson());
        } else {
            logger.error("调用远程查询订单退款信息失败，无法开始实时对账，原因：{}", response.getReturnMessage());
            reconcileManager.createFailRealTimeReconJob(response.getLocalOrder().getOrderNumber(), response.getReturnMessage(), ReconcileManager.refund_real_time);
        }
    }
}
