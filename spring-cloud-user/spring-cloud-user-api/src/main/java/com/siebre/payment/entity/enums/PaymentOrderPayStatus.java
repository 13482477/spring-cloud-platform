package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

public enum PaymentOrderPayStatus implements BaseEnum {
	
	UNPAID(1, "未支付"),

	//订单支付中，不可以重新发起支付
    PAYING(2, "支付中"),

	//订单支付成功状态下才可以申请退款
	PAID(3, "支付成功"),

	//订单关闭状态下，不可以退款
    CLOSED(4, "订单关闭"),

	//订单支付失败，用户可以重新发起支付
	PAYERROR(5, "支付失败"),
	;

	private PaymentOrderPayStatus(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	private int value;
	
	private String description;
	
	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
