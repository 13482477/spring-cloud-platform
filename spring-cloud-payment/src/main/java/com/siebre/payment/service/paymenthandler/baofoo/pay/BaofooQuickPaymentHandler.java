package com.siebre.payment.service.paymenthandler.baofoo.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymenttransaction.PaymentTransaction;
import com.siebre.payment.entity.paymentway.PaymentWay;
import com.siebre.payment.service.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.serviceinterface.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.serviceinterface.paymenthandler.payment.PaymentResponse;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class BaofooQuickPaymentHandler extends AbstractPaymentComponent {

    private Logger log = LoggerFactory.getLogger(BaofooQuickPaymentHandler.class);


    @Override
    protected PaymentResponse handleInternal(PaymentRequest request, PaymentWay paymentWay, PaymentOrder paymentOrder, PaymentTransaction paymentTransaction) {
        return null;
    }
}
