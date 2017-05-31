package com.siebre.payment.paymenthandler.baofoo.pay.dto;

import java.math.BigDecimal;

/**
 * Created by AdamTang on 2017/4/19.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class BaofooRequest {

    //请求地址
    private String requestUrl;

    //宝付业务流水号
    private String businessCode;
    //手机验证码
    private String smsCode;

    //内部交易流水号
    private String internalNumber;

    //宝付交易号
    private String externalNumber;

    //绑定卡
    private BindCard bindCard;
    //用户id
    private String userID;

    //预绑卡代码
    private String preBindCode;

    //订单金额
    private BigDecimal orderAmount;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }

    public BindCard getBindCard() {
        return bindCard;
    }

    public void setBindCard(BindCard bindCard) {
        this.bindCard = bindCard;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPreBindCode() {
        return preBindCode;
    }

    public void setPreBindCode(String preBindCode) {
        this.preBindCode = preBindCode;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
