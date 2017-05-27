package com.siebre.payment.controller.paymentgateway.order;

import java.io.Serializable;
import java.util.List;

import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;

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
