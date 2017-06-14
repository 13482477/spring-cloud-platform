package com.siebre.payment.paymentgateway.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 微信js api支付参数
 */
public class WechatJsApiParams implements Serializable {

    private String appId;

    private String nonceStr;

    @Deprecated
    private String paySign;

    @JsonProperty("package")
    private String packageSrt;

    private String signType;

    private Long timeStamp;

    public WechatJsApiParams() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    @Deprecated
    public String getPaySign() {
        return paySign;
    }

    @Deprecated
    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageSrt() {
        return packageSrt;
    }

    public void setPackageSrt(String packageSrt) {
        this.packageSrt = packageSrt;
    }
}
