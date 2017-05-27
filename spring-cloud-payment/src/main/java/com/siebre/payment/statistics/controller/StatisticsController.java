package com.siebre.payment.statistics.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.statistics.vo.DonutVo;
import com.siebre.payment.statistics.vo.PaymentChannelTransactionVo;

@RestController
public class StatisticsController {
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@RequestMapping(value = "/api/v1/statistics/successPaymentAmount", method = {RequestMethod.GET})
	public BigDecimal getSuccessPaymentAmount() {
		return this.paymentOrderService.getSuccessPaymentAmount();
	}

	@RequestMapping(value = "/api/v1/statistics/successPaymentCount", method = {RequestMethod.GET})
	public Integer getSuccessPaymentCount() {
		return this.paymentOrderService.getSuccessPaymentCount();
	}

	@RequestMapping(value = "/api/v1/statistics/faildPaymentAmount", method = {RequestMethod.GET})
	public BigDecimal getFaildPaymentAmount() {
		return this.paymentOrderService.getFaildPaymentAmount();
	}

	@RequestMapping(value = "/api/v1/statistics/faildPaymentCount", method = {RequestMethod.GET})
	public Integer getFaildPaymentCount() {
		return this.paymentOrderService.getFaildPaymentCount();
	}

	@RequestMapping(value = "/api/v1/statistics/conversionRate", method = {RequestMethod.GET})
	public BigDecimal getConversionRate() {
		return this.paymentOrderService.getConversionRate();
	}

	@RequestMapping(value = "/api/v1/statistics/totalAmountOfThisWeek", method = {RequestMethod.GET})
	public BigDecimal[] getTotalAmountOfThisWeek() {
		return this.paymentOrderService.getTotalAmountOfThisWeek();
	}

	@RequestMapping(value = "/api/v1/statistics/channelSuccessedCount", method = {RequestMethod.GET})
	public List<DonutVo> getChannelSuccessedCount() {
		return this.paymentOrderService.getChannelSuccessedCount();
	}

	@RequestMapping(value = "/api/v1/statistics/channelSuccessedAmount", method = {RequestMethod.GET})
	public List<DonutVo> getChannelSuccessedAmount() {
		return this.paymentOrderService.getChannelSuccessedAmount();
	}

	@RequestMapping(value = "/api/v1/statistics/paymentWaySuccessCount", method = {RequestMethod.GET})
	public List<List<Integer>> getPaymentWaySuccessCount() {
		return this.paymentOrderService.getPaymentWaySuccessCount();
	}

	@RequestMapping(value = "/api/v1/statistics/countPaymentChannelTransaction", method = {RequestMethod.GET})
	public List<PaymentChannelTransactionVo> countPaymentChannelTransaction() {
		return this.paymentOrderService.countPaymentChannelTransaction();
	}

}
