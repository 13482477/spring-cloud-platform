package com.siebre.payment.refundapplication.entity;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;

import java.math.BigDecimal;

/**
 * Created by AdamTang on 2017/4/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 退款申请
 * 每一笔经过iPay的退款 都需要创建退款申请，从退款申请->订单->交易->渠道
 */
public class RefundApplication extends BaseObject {
	
	private static final long serialVersionUID = 9017247646028696851L;

	//订单号
    private String orderNumber;

    private PaymentOrder paymentOrder;

    //退款金额
    private BigDecimal refundAmount;

    //退款申请号(内部,与退款的transaction的internalTransactionNumber关联)
    private String refundApplicationNumber;

    private PaymentTransaction refundTransaction;

    //退款申请 状态
    private RefundApplicationStatus status;

    //退款申请请求(原因)
    private String request;

    //退款处理返回（处理结果等）
    private String response;

    /**
     * 统一支付接口2.0字段
     * ------start-------
     */
    //前端唯一标识一次提交退款订单的字段
    private String messageId;

    //如果退款不能立即完成，支付平台通知前端退款结果的地址
    private String notificationUrl;

    //------end-------

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

    public String getRefundApplicationNumber() {
        return refundApplicationNumber;
    }

    public void setRefundApplicationNumber(String refundApplicationNumber) {
        this.refundApplicationNumber = refundApplicationNumber;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    @Override
    public String toString() {
        return "RefundApplication{" +
                "orderNumber='" + orderNumber + '\'' +
                ", refundAmount='" + refundAmount + '\'' +
                ", applicationNumber='" + refundApplicationNumber + '\'' +
                ", status=" + status +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                ", messageId='" + messageId + '\'' +
                ", notificationUrl='" + notificationUrl + '\'' +
                '}';
    }
}
