package com.siebre.payment.paymenthandler.paymentquery;

import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;

import java.util.Date;

public class PaymentQueryRequest {

    //交易渠道
    private PaymentChannel paymentChannel;

    //交易方式
    private PaymentWay paymentWay;

    //查询接口
    private PaymentInterface paymentInterface;

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public PaymentWay getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(PaymentWay paymentWay) {
        this.paymentWay = paymentWay;
    }

    public PaymentInterface getPaymentInterface() {
        return paymentInterface;
    }

    public void setPaymentInterface(PaymentInterface paymentInterface) {
        this.paymentInterface = paymentInterface;
    }

}
