package com.siebre.payment.statistics.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class DonutVo implements Serializable {

	private static final long serialVersionUID = 1L;

	public DonutVo() {
	}

	public DonutVo(String name, BigDecimal value) {
		this.name = name;
		this.value = value;
	}

	private String name;

	private BigDecimal value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
