package com.siebre.payment.paymentway.entity;


import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付方式
 *
 * @author lizhiqiang
 */
public class PaymentWay extends BaseObject {

    private static final long serialVersionUID = 577116574400454457L;

    /**
     * 支付渠道id
     */
    private Long paymentChannelId;

    /**
     * 支付渠道
     */
    private PaymentChannel paymentChannel;

    /**
     * 支付方式名称
     */
    private String name;

    /**
     * 支付方式代码
     */
    private String code;

    /**
     * 支付应用id
     */
    private String appId;

    /**
     * 加密方式
     */
    private EncryptionMode encryptionMode;

    /**
     * 自己的私钥
     */
    private String secretKey;

    /**
     * 支付方式的公钥
     */
    private String publicKey;

    /**
     * 支付方式的加密证书
     */
    private byte[] apiClientCertCer;

    /**
     * 支付方式加密证书的key
     */
    private byte[] apiClientCertPkcs;

    /**
     * 支付接口
     */
    private List<PaymentInterface> paymentInterfaces = new ArrayList<PaymentInterface>();

    public Long getPaymentChannelId() {
        return paymentChannelId;
    }

    public void setPaymentChannelId(Long paymentChannelId) {
        this.paymentChannelId = paymentChannelId;
    }

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<PaymentInterface> getPaymentInterfaces() {
        return paymentInterfaces;
    }

    public void setPaymentInterfaces(List<PaymentInterface> paymentInterfaces) {
        this.paymentInterfaces = paymentInterfaces;
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

    public byte[] getApiClientCertCer() {
        return apiClientCertCer;
    }

    public void setApiClientCertCer(byte[] apiClientCertCer) {
        this.apiClientCertCer = apiClientCertCer;
    }

    public byte[] getApiClientCertPkcs() {
        return apiClientCertPkcs;
    }

    public void setApiClientCertPkcs(byte[] apiClientCertPkcs) {
        this.apiClientCertPkcs = apiClientCertPkcs;
    }
}