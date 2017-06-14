package com.siebre.payment.paymentgateway.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huang Tianci
 * 支付接口-订单信息返回模型
 */
public class UnifiedPayResOrder implements Serializable {

    private String paymentWayCode;

    private Date createdOn;

    private String orderNumber;

    private Date paidOn;

    private String status;

    public String getPaymentWayCode() {
        return paymentWayCode;
    }

    public void setPaymentWayCode(String paymentWayCode) {
        this.paymentWayCode = paymentWayCode;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(Date paidOn) {
        this.paidOn = paidOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
