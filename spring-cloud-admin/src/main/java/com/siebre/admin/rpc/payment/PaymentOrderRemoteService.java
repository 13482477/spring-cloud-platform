package com.siebre.admin.rpc.payment;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.siebre.basic.web.WebResult;

@FeignClient(value = "SPRING-CLOUD-PAYMENT", fallback = PaymentOrderRemoteServiceFallback.class)
public interface PaymentOrderRemoteService {
	
	@RequestMapping(value = "/api/v1/paymentOrders", method = RequestMethod.GET)
	public WebResult<List<PaymentOrder>> find(
			@RequestParam("channelCodeList") String channelCodeList, 
			@RequestParam("showCount") Integer showCount,
			@RequestParam("currentPage") Integer currentPage,
			@RequestParam("orderNumber") String orderNumber,
			@RequestParam("refundNumber") String refundNumber,
			@RequestParam("startDate") Date startDate,
			@RequestParam("endDate") Date endDate
			);

}
