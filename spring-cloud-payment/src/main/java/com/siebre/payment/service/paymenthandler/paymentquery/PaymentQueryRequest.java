package com.siebre.payment.service.paymenthandler.paymentquery;

import com.siebre.payment.entity.paymentchannel.PaymentChannel;
import com.siebre.payment.entity.paymentinterface.PaymentInterface;
import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymenttransaction.PaymentTransaction;
import com.siebre.payment.entity.paymentway.PaymentWay;

public class PaymentQueryRequest {

    //订单号
    private String orderNumber;

    //内部流水号
    private String internalNumber;

    //外部流水号
    private String externalNumber;

    //交易渠道
    private PaymentChannel paymentChannel;

    //交易方式
    private PaymentWay paymentWay;

    //查询接口
    private PaymentInterface paymentInterface;

    //交易
    private PaymentTransaction paymentTransaction;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }


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

    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    @Override
    public String toString() {
        return "PaymentQueryRequest{" +
                "orderNumber='" + orderNumber + '\'' +
                ", internalNumber='" + internalNumber + '\'' +
                ", externalNumber='" + externalNumber + '\'' +
                ", paymentChannel='" + paymentChannel + '\'' +
                '}';
    }
}
