package com.siebre.payment.paymentchannel.vo;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 微信配置类
 */
public class WeChatConfigVo implements Serializable{

    private String merchantCode;

    private String appId;

    private String appSecret;

    //API密钥
    private String secretKey;

    //操作员账号
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
