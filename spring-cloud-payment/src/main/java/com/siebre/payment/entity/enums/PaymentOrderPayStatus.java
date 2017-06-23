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

	FULL_REFUND(7, "已退款"),

	INVALID(8, "订单失效"),

	REFUNDING(9, "退款已申请"),

	REFUNDERROR(10, "退款失败"), //退款失败状态是可能由支付成功或者部分退款转成的，表示最近一次退款操作失败的一个状态
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
