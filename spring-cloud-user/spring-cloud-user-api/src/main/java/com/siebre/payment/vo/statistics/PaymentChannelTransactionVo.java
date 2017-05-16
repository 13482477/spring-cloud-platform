package com.siebre.payment.vo.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentChannelTransactionVo implements Serializable {
	
	private static final long serialVersionUID = -2951011287151686410L;

	private Long channelId;
	
	private String channelName;
	
	private Integer successCount;
	
	private BigDecimal successAmount;
	
	private Integer faildCount;
	
	private Integer faildAmount;
	
	private BigDecimal conversionRate;

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public BigDecimal getSuccessAmount() {
		return successAmount;
	}

	public void setSuccessAmount(BigDecimal successAmount) {
		this.successAmount = successAmount;
	}

	public Integer getFaildCount() {
		return faildCount;
	}

	public void setFaildCount(Integer faildCount) {
		this.faildCount = faildCount;
	}

	public Integer getFaildAmount() {
		return faildAmount;
	}

	public void setFaildAmount(Integer faildAmount) {
		this.faildAmount = faildAmount;
	}

	public BigDecimal getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(BigDecimal conversionRate) {
		this.conversionRate = conversionRate;
	}

}
