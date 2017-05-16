package com.siebre.payment.vo.unionpayment;

import java.io.Serializable;
import java.math.BigDecimal;

public class DonutVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String label;
	
	private BigDecimal value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
