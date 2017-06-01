package com.siebre.payment.refundapplication.dto;

import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
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
     * 原始支付交易
     */
    private PaymentTransaction paymentTransaction;

    /**
     * 原始订单
     */
    private PaymentOrder paymentOrder;

    public String getOriginInternalNumber() {
        return originInternalNumber;
    }

    public void setOriginInternalNumber(String originInternalNumber) {
        this.originInternalNumber = originInternalNumber;
    }

    public RefundApplication getRefundApplication() {
        return refundApplication;
    }

    public String getOriginExternalNumber() {
        return originExternalNumber;
    }

    public void setOriginExternalNumber(String originExternalNumber) {
        this.originExternalNumber = originExternalNumber;
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

    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }
}
