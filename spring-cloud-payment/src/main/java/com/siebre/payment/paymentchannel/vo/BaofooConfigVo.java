package com.siebre.payment.paymentchannel.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by meilan on 2017/6/2.
 * 宝付配置信息VO模型
 */
public class BaofooConfigVo implements Serializable {
    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户号", required = true)
    private String merchantCode;

    /**
     * 商户终端号
     */
    @ApiModelProperty(value = "商户终端号", required = true)
    private String terminalId;

    /**
     * 商户私钥密码
     */
    @ApiModelProperty(value = "私钥密码", required = true)
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
