package com.siebre.payment.restful.paymentgateway.query;

import java.io.Serializable;

public class PaymentOrderQueryRequest implements Serializable {

    private String orderNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
