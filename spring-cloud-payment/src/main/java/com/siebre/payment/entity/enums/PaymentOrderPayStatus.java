package com.siebre.payment.entity.enums;

import com.siebre.basic.enumutil.BaseEnum;

/**
 * @author Huang Tianci
 * 订单支付状态
 */
public enum PaymentOrderPayStatus implements BaseEnum {
	
	UNPAID(1, "待支付"),

    PAYING(2, "支付中"),

	PAID(3, "支付成功"),

	PAYERROR(4, "支付失败"),

	PROCESSING_REFUND(5, "退款中"),

	PART_REFUND(6, "部分退款"),

	FULL_REFUND(7, "全额退款"),

	INVALID(8, "订单失效"),
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
