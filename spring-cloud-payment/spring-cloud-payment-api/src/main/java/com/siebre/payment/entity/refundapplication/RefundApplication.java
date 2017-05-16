package com.siebre.payment.entity.refundapplication;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymenttransaction.PaymentTransaction;

import java.math.BigDecimal;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 退款申请
 * 每一笔经过iPay的退款 都需要创建退款申请，从退款申请->订单->交易->渠道
 */
public class RefundApplication extends BaseObject {
    //订单号
    private String orderNumber;

    private PaymentOrder paymentOrder;

    //退款金额
    private BigDecimal refundAmount;

    //退款申请号(内部,与退款的transaction的internalTransactionNumber关联)
    private String applicationNumber;

    private PaymentTransaction refundTransaction;

    //退款申请 状态
    private RefundApplicationStatus status;

    //退款申请请求(原因)
    private String request;

    //退款处理返回（处理结果等）
    private String response;

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public PaymentTransaction getRefundTransaction() {
        return refundTransaction;
    }

    public void setRefundTransaction(PaymentTransaction refundTransaction) {
        this.refundTransaction = refundTransaction;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public RefundApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(RefundApplicationStatus status) {
        this.status = status;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "RefundApplication{" +
                "orderNumber='" + orderNumber + '\'' +
                ", refundAmount='" + refundAmount + '\'' +
                ", applicationNumber='" + applicationNumber + '\'' +
                ", status=" + status +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
