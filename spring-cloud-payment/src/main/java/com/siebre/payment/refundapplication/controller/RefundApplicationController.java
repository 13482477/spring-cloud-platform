package com.siebre.payment.refundapplication.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.entity.enums.RefundApplicationStatus;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.refundapplication.service.RefundApplicationService;

@RestController
public class RefundApplicationController {
	
	@Autowired
	private RefundApplicationService refundApplicationService;

	@RequestMapping(value = "/api/v1/refundApplications", method = {RequestMethod.GET})
	public WebResult<List<RefundApplication>> selectRefundList(
																@RequestParam String orderNumber, 
																@RequestParam String refundNumber, 
																@RequestParam String channelName, 
																@RequestParam RefundApplicationStatus refundStatus, 
																@RequestParam Date startDate, 
																@RequestParam Date endDate,
																@RequestParam int page, 
																@RequestParam int limit, 
																@RequestParam String sortField, 
																@RequestParam String order
			) {
		PageInfo pageInfo = new PageInfo(limit, page, sortField, order);
		List<RefundApplication> list = refundApplicationService.selectRefundList(orderNumber, refundNumber, channelName, refundStatus, startDate, endDate, pageInfo);
		return WebResult.<List<RefundApplication>>builder().returnCode(WebResult.SUCCESS_CODE).data(list).build();
	}

	
	@RequestMapping(value = "/api/v1/refundApplication/paymentOrder/{orderNumber}", method= {RequestMethod.GET})
	public WebResult<RefundApplication> getRefundApplicationByOrderNumber(@PathVariable String orderNumber) {
		RefundApplication data = this.refundApplicationService.getRefundApplicationByOrderNumber(orderNumber);
		return WebResult.<RefundApplication>builder().returnCode(WebResult.SUCCESS_CODE).data(data).build();
	}
	
	@RequestMapping(value = "/api/v1/refundApplication/{refundApplicationNumber}")
	public WebResult<RefundApplication> getRefundApplicationByRefundApplicationNumber(@PathVariable String refundApplicationNumber) {
		RefundApplication data = this.refundApplicationService.getRefundApplicationByRefundApplicationNumber(refundApplicationNumber);
		return WebResult.<RefundApplication>builder().returnCode(WebResult.SUCCESS_CODE).data(data).build();
	}

}
