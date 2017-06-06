package com.siebre.payment.paymentchannel.vo;

import java.io.Serializable;

/**
 * Created by meilan on 2017/6/2.
 * 宝付配置信息VO模型
 */
public class BaofooConfigVo implements Serializable {
    /**
     * 商户id
     */
    private String merchantCode;

    /**
     * 商户终端号
     */
    private String terminalId;

    /**
     * 商户私钥密码
     */
    private String secretKey;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
