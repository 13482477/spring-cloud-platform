package com.siebre.payment.refundapplication.dto;

import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.entity.RefundApplication;

/**
 * 退款请求DTO
 */
public class PaymentRefundRequest {

    //原始支付交易的内部流水号
    private String originInternalNumber;

    //原始支付交易的外部流水号
    private String originExternalNumber;

    /**
     * 退款申请
     */
    private RefundApplication refundApplication;

    /**
     * 退款新创建的交易
     */
    private PaymentTransaction refundTransaction;

    /**
     * 原始订单
     */
    private PaymentOrder paymentOrder;

    private PaymentWay paymentWay;

    private PaymentInterface paymentInterface;

    public String getOriginInternalNumber() {
        return originInternalNumber;
    }

    public void setOriginInternalNumber(String originInternalNumber) {
        this.originInternalNumber = originInternalNumber;
    }

    public String getOriginExternalNumber() {
        return originExternalNumber;
    }

    public void setOriginExternalNumber(String originExternalNumber) {
        this.originExternalNumber = originExternalNumber;
    }

    public RefundApplication getRefundApplication() {
        return refundApplication;
    }

    public void setRefundApplication(RefundApplication refundApplication) {
        this.refundApplication = refundApplication;
    }

    public PaymentTransaction getRefundTransaction() {
        return refundTransaction;
    }

    public void setRefundTransaction(PaymentTransaction refundTransaction) {
        this.refundTransaction = refundTransaction;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
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
