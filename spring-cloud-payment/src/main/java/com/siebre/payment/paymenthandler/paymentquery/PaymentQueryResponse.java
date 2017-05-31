package com.siebre.payment.paymenthandler.paymentquery;

import java.math.BigDecimal;

import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymentorder.entiry.PaymentOrder;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;

public class PaymentQueryResponse {

    private PaymentOrder order;

    private PaymentTransaction transaction;

    private PaymentTransactionStatus status;

    private BigDecimal orderAmount;

    public PaymentOrder getOrder() {
        return order;
    }

    public void setOrder(PaymentOrder order) {
        this.order = order;
    }

    public PaymentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    public PaymentTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentTransactionStatus status) {
        this.status = status;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Override
    public String toString() {
        return "PaymentQueryResponse{" +
                "order=" + order +
                ", transaction=" + transaction +
                ", status=" + status +
                ", orderAmount=" + orderAmount +
                '}';
    }
}
