package com.siebre.payment.controller.paymentgateway.order;

import com.siebre.payment.entity.paymentorderitem.PaymentOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author Huang Tianci
 */
public class PaymentOrderRequest implements Serializable{

    private String paymentWayCode;

    private List<PaymentOrderItem> paymentOrderItems;

    public String getPaymentWayCode() {
        return paymentWayCode;
    }

    public void setPaymentWayCode(String paymentWayCode) {
        this.paymentWayCode = paymentWayCode;
    }

    public List<PaymentOrderItem> getPaymentOrderItems() {
        return paymentOrderItems;
    }

    public void setPaymentOrderItems(List<PaymentOrderItem> paymentOrderItems) {
        this.paymentOrderItems = paymentOrderItems;
    }
}
