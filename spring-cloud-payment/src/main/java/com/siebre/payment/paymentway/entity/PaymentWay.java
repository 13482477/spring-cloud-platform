package com.siebre.payment.paymentway.entity;

import java.util.ArrayList;
import java.util.List;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.EncryptionMode;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;

/**
 * 支付方式
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
	private String creditPath;

	/**
	 * 支付方式加密证书的key
	 */
	private String creditKey;

	/**
     * 支付网关地址
     */
    private String paymentGatewayUrl;

    /**
     * 支付回调地址
     */
    private String paymentCallbackUrl;
    
    /**
     * 页面回调地址
     */
    private String paymentReturnPageUrl;
	
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

	public String getPaymentGatewayUrl() {
		return paymentGatewayUrl;
	}

	public void setPaymentGatewayUrl(String paymentGatewayUrl) {
		this.paymentGatewayUrl = paymentGatewayUrl;
	}

	public String getPaymentCallbackUrl() {
		return paymentCallbackUrl;
	}

	public void setPaymentCallbackUrl(String paymentCallbackUrl) {
		this.paymentCallbackUrl = paymentCallbackUrl;
	}

	public String getPaymentReturnPageUrl() {
		return paymentReturnPageUrl;
	}

	public void setPaymentReturnPageUrl(String paymentReturnPageUrl) {
		this.paymentReturnPageUrl = paymentReturnPageUrl;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getCreditPath() {
		return creditPath;
	}

	public void setCreditPath(String creditPath) {
		this.creditPath = creditPath;
	}

	public String getCreditKey() {
		return creditKey;
	}

	public void setCreditKey(String creditKey) {
		this.creditKey = creditKey;
	}
}