package com.siebre.payment.utils.txserial.model;

import java.util.Date;

public class TxSerialNumber {
    private String serialName;

    private Long currentValue;

    private Integer step;

    private String prefix;

    private Date currentDate;

    private Integer numberLength;

    public String getSerialName() {
        return serialName;
    }

    public void setSerialName(String serialName) {
        this.serialName = serialName == null ? null : serialName.trim();
    }

    public Long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? null : prefix.trim();
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Integer getNumberLength() {
        return numberLength;
    }

    public void setNumberLength(Integer numberLength) {
        this.numberLength = numberLength;
    }
}