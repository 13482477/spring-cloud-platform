package com.siebre.payment.paymenthandler.baofoo.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siebre.payment.paymenthandler.basic.payment.AbstractPaymentComponent;
import com.siebre.payment.paymenthandler.payment.PaymentRequest;
import com.siebre.payment.paymenthandler.payment.PaymentResponse;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;

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
