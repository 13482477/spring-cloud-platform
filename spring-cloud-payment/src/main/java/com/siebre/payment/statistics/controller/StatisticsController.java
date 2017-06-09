package com.siebre.payment.statistics.controller;

import com.siebre.basic.service.ServiceResult;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.entity.enums.ReturnCode;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.statistics.vo.DonutVo;
import com.siebre.payment.statistics.vo.PaymentChannelTransactionVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class StatisticsController {
	
	@Autowired
	private PaymentOrderService paymentOrderService;

	@ApiOperation(value="账户总览", notes = "账户总览")
	@RequestMapping(value = "/api/v1/statistics/total", method = GET)
	public WebResult<List<Map<String, Object>>> getPaymentCountInfo(){
		List<Map<String, Object>> result = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "成功交易额");
		map1.put("value", this.paymentOrderService.getSuccessPaymentAmount().getData());
		result.add(map1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("name", "成功交易笔数");
		map2.put("value", this.paymentOrderService.getSuccessPaymentCount().getData());
		result.add(map2);
		Map<String, Object> map3 = new HashMap<>();
		map3.put("name", "失败交易额");
		map3.put("value", this.paymentOrderService.getFaildPaymentAmount().getData());
		result.add(map3);
		Map<String, Object> map4 = new HashMap<>();
		map4.put("name", "失败交易笔数");
		map4.put("value", this.paymentOrderService.getFaildPaymentCount().getData());
		result.add(map4);
		return WebResult.<List<Map<String, Object>>>builder().data(result).returnCode(ReturnCode.SUCCESS.getValue()).build();
	}

	@ApiOperation(value="订单转换率", notes = "订单转换率")
	@RequestMapping(value = "/api/v1/statistics/conversionRate", method = GET)
	public WebResult<BigDecimal> getConversionRate() {
		ServiceResult<BigDecimal> serviceResult = this.paymentOrderService.getConversionRate();
		return serviceResult.convertWebResult();
	}

	@ApiOperation(value="本周每日交易金额", notes = "本周每日交易金额")
	@RequestMapping(value = "/api/v1/statistics/totalAmountOfThisWeek", method = GET)
	public WebResult<BigDecimal[]> getTotalAmountOfThisWeek() {
		ServiceResult<BigDecimal[]> serviceResult = this.paymentOrderService.getTotalAmountOfThisWeek();
		return serviceResult.convertWebResult();
	}

	@ApiOperation(value="渠道交易分布-成功订单笔数", notes = "渠道交易分布-成功订单笔数")
	@RequestMapping(value = "/api/v1/statistics/donutChannelSuccessedCount", method = GET)
	public WebResult<List<DonutVo>> getDonutChannelSuccessedCount() {
		ServiceResult<List<DonutVo>> serviceResult = this.paymentOrderService.getChannelSuccessedCount();
		return serviceResult.convertWebResult();
	}

	@ApiOperation(value="渠道交易分布-成功交易额", notes = "渠道交易分布-成功交易额")
	@RequestMapping(value = "/api/v1/statistics/donutChannelSuccessedAmount", method = GET)
	public WebResult<List<DonutVo>> getDonutChannelSuccessedAmount() {
		ServiceResult<List<DonutVo>> serviceResult = this.paymentOrderService.getChannelSuccessedAmount();
		return serviceResult.convertWebResult();
	}

	@ApiOperation(value="渠道交易分布-失败订单笔数", notes = "渠道交易分布-失败订单笔数")
	@RequestMapping(value = "/api/v1/statistics/donutChannelFailCount", method = GET)
	public WebResult<List<DonutVo>> getDonutChannelFailCount() {
		ServiceResult<List<DonutVo>> serviceResult = this.paymentOrderService.getChannelFailCount();
		return serviceResult.convertWebResult();
	}

	@ApiOperation(value="渠道交易分布-失败交易额", notes = "渠道交易分布-失败交易额")
	@RequestMapping(value = "/api/v1/statistics/donutChannelFailAmount", method = GET)
	public WebResult<List<DonutVo>> getDonutChannelFailAmount(){
		ServiceResult<List<DonutVo>> serviceResult = this.paymentOrderService.getChannelFailAmount();
		return serviceResult.convertWebResult();
	}

	@ApiOperation(value="渠道交易分布-支付方式分布", notes = "渠道交易分布-支付方式分布")
	@RequestMapping(value = "/api/v1/statistics/paymentCountByWay", method = GET)
	public WebResult<Map<String,List<DonutVo>>> getPaymentCountByWay() {
		ServiceResult<Map<String,List<DonutVo>>> serviceResult = this.paymentOrderService.getPaymentWaySuccessCount();
		return serviceResult.convertWebResult();
	}

	@ApiOperation(value="渠道交易明细", notes = "渠道交易明细")
	@RequestMapping(value = "/api/v1/statistics/countPaymentChannelTransaction", method = GET)
	public WebResult<List<PaymentChannelTransactionVo>> countPaymentChannelTransaction() {
		ServiceResult<List<PaymentChannelTransactionVo>> serviceResult = this.paymentOrderService.countPaymentChannelTransaction();
		return serviceResult.convertWebResult();
	}

}
