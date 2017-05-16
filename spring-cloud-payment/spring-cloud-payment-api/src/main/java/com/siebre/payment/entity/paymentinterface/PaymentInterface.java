package com.siebre.payment.entity.paymentinterface;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.paymentway.PaymentWay;

public class PaymentInterface extends BaseObject {

	private static final long serialVersionUID = -3631679891858117512L;

	/**
	 * 支付方式ID
	 */
    private Long paymentWayId;
    
    /**
     * 支付方式
     */
    private PaymentWay paymentWay;

    /**
     * 支付接口名称
     */
    private String interfaceName;

    /**
     * 支付接口代码
     */
    private String interfaceCode;
    
    /**
     * 接口地址
     */
    private String requestUrl;

	/**
	 * 异步回调地址
     */
    private String callbackUrl;

	/**
	 * 同步回调页面
     */
    private String returnPageUrl;

    /**
     * 接口类型
     */
    private PaymentInterfaceType paymentInterfaceType;
    
    /**
     * 处理器bean name
     */
    private String handlerBeanName;
    

	public Long getPaymentWayId() {
		return paymentWayId;
	}

	public void setPaymentWayId(Long paymentWayId) {
		this.paymentWayId = paymentWayId;
	}

	public PaymentWay getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(PaymentWay paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public PaymentInterfaceType getPaymentInterfaceType() {
		return paymentInterfaceType;
	}

	public void setPaymentInterfaceType(PaymentInterfaceType paymentInterfaceType) {
		this.paymentInterfaceType = paymentInterfaceType;
	}

	public String getHandlerBeanName() {
		return handlerBeanName;
	}

	public void setHandlerBeanName(String handlerBeanName) {
		this.handlerBeanName = handlerBeanName;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getReturnPageUrl() {
		return returnPageUrl;
	}

	public void setReturnPageUrl(String returnPageUrl) {
		this.returnPageUrl = returnPageUrl;
	}
}