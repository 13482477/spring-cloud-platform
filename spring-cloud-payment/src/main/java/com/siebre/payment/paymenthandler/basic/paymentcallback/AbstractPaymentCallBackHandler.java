package com.siebre.payment.paymenthandler.basic.paymentcallback;

import com.siebre.payment.billing.amqp.RealTimeReconcileProduct;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by AdamTang on 2017/4/17.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public abstract class AbstractPaymentCallBackHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PaymentTransactionService paymentTransactionService;

    @Autowired
    protected PaymentWayService paymentWayService;

    @Autowired
    protected RealTimeReconcileProduct realTimeReconcileProduct;


    public Object callBackHandle(HttpServletRequest request, HttpServletResponse response,PaymentInterface paymentInterface){
        logger.info("callBackHandler handle request");

        return this.callBackHandleInternal(request,response,paymentInterface);
    }

    protected abstract Object callBackHandleInternal(HttpServletRequest request,HttpServletResponse response,PaymentInterface paymentInterface);

}
