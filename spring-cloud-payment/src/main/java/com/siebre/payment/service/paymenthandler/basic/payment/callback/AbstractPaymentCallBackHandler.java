package com.siebre.payment.service.paymenthandler.basic.payment.callback;

import com.siebre.payment.entity.paymentinterface.PaymentInterface;
import com.siebre.payment.service.paymenttransaction.PaymentTransactionService;
import com.siebre.payment.service.paymentway.PaymentWayService;
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


    public Object callBackHandle(HttpServletRequest request, HttpServletResponse response,PaymentInterface paymentInterface){
        logger.info("callBackHandler handle request");

        return this.callBackHandleInternal(request,response,paymentInterface);
    }

    protected abstract Object callBackHandleInternal(HttpServletRequest request,HttpServletResponse response,PaymentInterface paymentInterface);

}
