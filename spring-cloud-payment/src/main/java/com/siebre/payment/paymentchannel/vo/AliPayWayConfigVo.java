package com.siebre.payment.paymentchannel.vo;

import com.siebre.payment.entity.enums.EncryptionMode;

import java.io.Serializable;

/**
 * @author Huang Tianci
 * 支付宝-支付方式配置VO模型
 */
public class AliPayWayConfigVo implements Serializable {

    private String appId;

    private EncryptionMode encryptionMode;

    private String secretKey;

    private String publicKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public EncryptionMode getEncryptionMode() {
        return encryptionMode;
    }

    public void setEncryptionMode(EncryptionMode encryptionMode) {
        this.encryptionMode = encryptionMode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
