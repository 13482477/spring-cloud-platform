package com.siebre.payment.refundapplication.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentOrderRefundStatus;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymentorder.vo.OrderQueryParamsVo;
import com.siebre.payment.paymentorder.vo.Refund;
import com.siebre.payment.paymentorder.vo.TradeOrder;
import com.siebre.payment.refundapplication.service.RefundApplicationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class RefundApplicationController {
	
	@Autowired
	private RefundApplicationService refundApplicationService;

	@Autowired
	private PaymentOrderService paymentOrderService;

	@ApiOperation(value = "退款列表", notes = "退款列表")
	@RequestMapping(value = "/api/v1/refundApplications", method = GET)
	public WebResult<List<Refund>> queryForPage(OrderQueryParamsVo paramsVo) {
		PageInfo page = new PageInfo();
		page.setCurrentPage(paramsVo.getCurrentPage());
		page.setShowCount(paramsVo.getShowCount());
		List<Refund> refunds = refundApplicationService.qeuryRefundByPage(paramsVo, page);
		return WebResult.<List<Refund>>builder().returnCode(WebResult.SUCCESS_CODE).data(refunds).pageInfo(page).build();
	}

	@ApiOperation(value = "申请退款列表", notes = "申请退款列表")
	@RequestMapping(value = "/listForSingleRefund", method = GET)
	public WebResult<List<TradeOrder>> queryForSingleRefundByPage(OrderQueryParamsVo paramsVo) {
		PageInfo page = new PageInfo();
		page.setCurrentPage(paramsVo.getCurrentPage());
		page.setShowCount(paramsVo.getShowCount());
		paramsVo.getPayStatusList().add(PaymentOrderPayStatus.PAID);
		paramsVo.getRefundStatusList().add(PaymentOrderRefundStatus.NOT_REFUND);
		List<TradeOrder> tradeOrders = paymentOrderService.queryOrderByPage(paramsVo, page);
		return WebResult.<List<TradeOrder>>builder().returnCode(WebResult.SUCCESS_CODE).data(tradeOrders).pageInfo(page).build();
	}

}
