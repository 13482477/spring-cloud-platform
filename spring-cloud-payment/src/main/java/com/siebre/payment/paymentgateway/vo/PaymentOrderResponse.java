package com.siebre.payment.paymentgateway.vo;


import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author Huang Tianci
 */
public class PaymentOrderResponse implements Serializable {

    private String returnCode;

    private String returnMessage;

    private PaymentOrder paymentOrder;

    private PaymentOrderResponse(String returnCode, String returnMessage, PaymentOrder paymentOrder) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.paymentOrder = paymentOrder;
    }

    public static PaymentOrderResponse FAIL(String s) {
        return new PaymentOrderResponse(ReturnCode.FAIL.getDescription(), s, null);
    }

    public static PaymentOrderResponse SUCCESS(String s, PaymentOrder paymentOrder) {
        return new PaymentOrderResponse(ReturnCode.SUCCESS.getDescription(), s, paymentOrder);
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

}
