package com.siebre.payment.paymentgateway.vo;


import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author Huang Tianci
 */
public class PaymentOrderRequest implements Serializable {

    /**
     * 前端在一次创建订单中唯一标识订单的字段，由前端生成，后台保存
     */
    private String messageId;

    private String paymentWayCode;

    private List<PaymentOrderItem> paymentOrderItems;

    private String sellingChannel;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSellingChannel() {
        return sellingChannel;
    }

    public void setSellingChannel(String sellingChannel) {
        this.sellingChannel = sellingChannel;
    }

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
