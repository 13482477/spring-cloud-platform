package com.siebre.payment.vo.unionpayment;

import java.io.Serializable;

public class BasePayNotifyParamVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3637982679411889264L;
	private String payWayCode;

	public String getPayWayCode() {
		return payWayCode;
	}

	public void setPayWayCode(String payWayCode) {
		this.payWayCode = payWayCode;
	}
}
