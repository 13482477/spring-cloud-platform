package com.siebre.product.messagedemo.controller.messageobject;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;

public class QuoteResult {

	String status = "Success";
	List<String> messages = Lists.newArrayList();
	BigDecimal premium;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public BigDecimal getPremium() {
		return premium;
	}

	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}
	
	public QuoteResultBuilder builder() {
		return new QuoteResultBuilder();
	}
	
	public static class QuoteResultBuilder {
		
		String status = "Success";
		List<String> messages = Lists.newArrayList();
		BigDecimal insuredAmount;
		BigDecimal premium;
		
		public QuoteResultBuilder status(String status) {
			this.status = status;
			return this;
		}
		
		public QuoteResultBuilder messages(List<String> messages) {
			this.messages = messages;
			return this;
		}
		
		public QuoteResultBuilder insuredAmount(BigDecimal insuredAmount) {
			this.insuredAmount = insuredAmount;
			return this;
		}
		
		public QuoteResultBuilder premium(BigDecimal premium) {
			this.premium = premium;
			return this;
		}
		
		public QuoteResult build() {
			QuoteResult quoteResult = new QuoteResult();
			quoteResult.setStatus(this.status);
			quoteResult.setMessages(this.messages);
			quoteResult.setPremium(this.premium);
			
			return quoteResult;
		}
		
	}
	
}
