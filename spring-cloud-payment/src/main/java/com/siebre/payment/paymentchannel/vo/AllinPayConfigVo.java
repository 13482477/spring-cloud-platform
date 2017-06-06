package com.siebre.payment.paymentchannel.vo;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Jiang Meilan
 * 通联配置信息VO模型
 */
public class AllinPayConfigVo implements Serializable {
    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户号", required = true)
    private String merchantCode;

    /**
     * 商户用户名
     */
    @ApiModelProperty(value = "商户用户名", required = true)
    private String merchantName;

    /**
     * 商户用户密码
     */
    @ApiModelProperty(value = "商户用户密码", required = true)
    private String merchantPwd;

    /**
     * 自己的签名私钥
     */
    @ApiModelProperty(value = "私钥密码", required = true)
    private String secretKey;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantPwd() {
        return merchantPwd;
    }

    public void setMerchantPwd(String merchantPwd) {
        this.merchantPwd = merchantPwd;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
