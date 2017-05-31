package com.siebre.payment.policylibility.entity;

import com.siebre.basic.model.BaseObject;

import java.math.BigDecimal;

public class PolicyLibility extends BaseObject{

    private Long orderItemId;

    private String libilityName;

    private BigDecimal insuredAmount;

    private BigDecimal premium;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getLibilityName() {
        return libilityName;
    }

    public void setLibilityName(String libilityName) {
        this.libilityName = libilityName == null ? null : libilityName.trim();
    }

    public BigDecimal getInsuredAmount() {
        return insuredAmount;
    }

    public void setInsuredAmount(BigDecimal insuredAmount) {
        this.insuredAmount = insuredAmount;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }
}