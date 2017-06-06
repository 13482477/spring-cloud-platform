package com.siebre.payment.paymentchannel.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 微信配置类
 */
public class WeChatConfigVo implements Serializable{

    @ApiModelProperty(value = "商户号", required = true)
    private String merchantCode;

    @ApiModelProperty(value = "AppId", required = true)
    private String appId;

    @ApiModelProperty(value = "AppSecret", required = true)
    private String appSecret;

    @ApiModelProperty(value = "API密钥", required = true)
    private String secretKey;

    @ApiModelProperty(value = "操作员账号", required = true)
    private String opUesrId;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getOpUesrId() {
        return opUesrId;
    }

    public void setOpUesrId(String opUesrId) {
        this.opUesrId = opUesrId;
    }
}
