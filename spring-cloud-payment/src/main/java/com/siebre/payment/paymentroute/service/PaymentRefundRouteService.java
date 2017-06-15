package com.siebre.payment.paymentroute.service;

import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymenthandler.basic.paymentrefund.AbstractPaymentRefundComponent;
import com.siebre.payment.paymenthandler.config.HandlerBeanNameConfig;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.refundapplication.dto.PaymentRefundRequest;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 退款路由
 * 通过路由交给具体的退款handler处理退款
 */
@Service
public class PaymentRefundRouteService {

    private Logger logger = LoggerFactory.getLogger(PaymentRefundRouteService.class);

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private PaymentWayService paymentWayService;

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    public void route(PaymentRefundRequest paymentRefundRequest, PaymentRefundResponse refundResponse) {

        PaymentOrder paymentOrder = paymentOrderService.queryPaymentOrder(paymentRefundRequest.getRefundApplication().getOrderNumber());
        paymentRefundRequest.setPaymentOrder(paymentOrder);

        PaymentTransaction paymentTransaction = paymentTransactionService.getSuccessPaidPaymentTransaction(paymentOrder.getOrderNumber());
        paymentRefundRequest.setPaymentTransaction(paymentTransaction);

        PaymentWay paymentWay = paymentWayService.getPaymentWay(paymentTransaction.getPaymentWay().getCode());

        //针对同一渠道下不同支付方式使用统一退款接口的情况，需要做处理。先在该支付方式下查找，是否存在paymentInterface，如果不存在，则在该支付方式所在渠道下查找paymentInterface
        PaymentInterface paymentInterface = paymentWayService.getPaymentInterface(paymentWay.getCode(), PaymentInterfaceType.REFUND);
        if (paymentInterface == null) {
            logger.info("支付方式{}下没有找到对应的退款handler,在该支付方式对应的渠道下查找", paymentWay.getName());
            List<PaymentWay> ways = paymentWayService.getPaymentWayByChannelId(paymentWay.getPaymentChannelId());
            for (PaymentWay way : ways) {
                paymentInterface = paymentWayService.getPaymentInterface(way.getCode(), PaymentInterfaceType.REFUND);
                if (paymentInterface != null) {
                    //使用该interface对应的paymentway
                    paymentWay = paymentWayService.getPaymentWay(way.getCode());
                    break;
                }
            }
        }

        String handleBeanName = HandlerBeanNameConfig.REFUND_MAPPING.get(paymentWay.getCode());
        logger.info("加载" + handleBeanName);
        AbstractPaymentRefundComponent paymentRefundHandler = (AbstractPaymentRefundComponent) SpringContextUtil.getBean(handleBeanName);

        paymentRefundHandler.handle(paymentRefundRequest, refundResponse, paymentTransaction, paymentOrder, paymentWay, paymentInterface);

    }
}
