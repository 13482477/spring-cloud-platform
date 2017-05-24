package com.siebre.payment.restful.paymentgateway.order;

import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymentorderitem.PaymentOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author Huang Tianci
 */
public class PaymentOrderResponse implements Serializable {

    private String returnCode;

    private String message;

    private PaymentOrder paymentOrder;

    private List<PaymentOrderItem> paymentOrderItems;

    private PaymentOrderResponse(String returnCode, String message, PaymentOrder paymentOrder, List<PaymentOrderItem> paymentOrderItems) {
        this.returnCode = returnCode;
        this.message = message;
        this.paymentOrder = paymentOrder;
        this.paymentOrderItems = paymentOrderItems;
    }

    public static PaymentOrderResponse FAIL(String s) {
        return new PaymentOrderResponse("500", s , null ,null);
    }

    public static PaymentOrderResponse SUCCESS(String s,PaymentOrder paymentOrder,List<PaymentOrderItem> paymentOrderItems) {
        return new PaymentOrderResponse("200", s , paymentOrder,paymentOrderItems);
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public List<PaymentOrderItem> getPaymentOrderItems() {
        return paymentOrderItems;
    }

    public void setPaymentOrderItems(List<PaymentOrderItem> paymentOrderItems) {
        this.paymentOrderItems = paymentOrderItems;
    }
}
