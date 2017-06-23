package com.siebre.payment.hostconfig.entity;

public class PaymentHostConfig {
    private Long id;

    private String paymentHost;

    private String frontHost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentHost() {
        return paymentHost;
    }

    public void setPaymentHost(String paymentHost) {
        this.paymentHost = paymentHost == null ? null : paymentHost.trim();
    }

    public String getFrontHost() {
        return frontHost;
    }

    public void setFrontHost(String frontHost) {
        this.frontHost = frontHost == null ? null : frontHost.trim();
    }
}