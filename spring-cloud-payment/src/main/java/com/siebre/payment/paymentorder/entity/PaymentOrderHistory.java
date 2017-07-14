package com.siebre.payment.paymentorder.entity;

public class PaymentOrderHistory {
    private Long id;

    private String ordernumber;

    private Integer version;

    private String paymentchannel;

    private String paymentway;

    private String orderjson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber == null ? null : ordernumber.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPaymentchannel() {
        return paymentchannel;
    }

    public void setPaymentchannel(String paymentchannel) {
        this.paymentchannel = paymentchannel == null ? null : paymentchannel.trim();
    }

    public String getPaymentway() {
        return paymentway;
    }

    public void setPaymentway(String paymentway) {
        this.paymentway = paymentway == null ? null : paymentway.trim();
    }

    public String getOrderjson() {
        return orderjson;
    }

    public void setOrderjson(String orderjson) {
        this.orderjson = orderjson == null ? null : orderjson.trim();
    }
}