package com.siebre.payment.paymentway.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.service.PaymentInterfaceService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class PaymentWayController {

	@Autowired
	private PaymentWayService paymentWayService;

	@Autowired
	private PaymentInterfaceService paymentInterfaceService;

	@ApiOperation(value = "根据分页信息查询支付方式", notes = "根据分页信息查询支付方式")
	@RequestMapping(value = "/api/v1/paymentWays", method = GET)
	public ServiceResult<List<PaymentWay>> list(PageInfo pageInfo) {
		return this.paymentWayService.selectAllByPage(pageInfo);
	}

	@ApiOperation(value = "创建支付方式", notes = "创建支付方式")
	@RequestMapping(value = "/api/v1/paymentWays", method = POST)
	public ServiceResult<PaymentWay> create(@RequestBody PaymentWay paymentWay) {
		return paymentWayService.createPaymentWay(paymentWay);
	}

	@ApiOperation(value = "根据主键值更新支付方式", notes = "根据主键值更新支付方式")
	@RequestMapping(value = "/api/v1/paymentWays/{paymentWayId}", method = PUT)
	public ServiceResult<PaymentWay> update(@PathVariable Long paymentWayId, @RequestBody PaymentWay paymentWay) {
		paymentWay.setId(paymentWayId);
		return paymentWayService.updatePaymentWay(paymentWay);
	}

	@ApiOperation(value = "查询支付方式详细信息", notes = "查询支付方式详细信息")
	@RequestMapping(value = "/api/v1/paymentWays/{paymentWayId}", method = GET)
	public ServiceResult<PaymentWay> detail(@PathVariable Long paymentWayId) {
		return paymentWayService.getPaymentWayById(paymentWayId);
	}

	@ApiOperation(value = "删除支付方式", notes = "删除支付方式")
	@RequestMapping(value = "/api/v1/paymentWays/{paymentWayId}", method = DELETE)
	public ServiceResult delete(@PathVariable Long paymentWayId) {
		return paymentWayService.deletePaymentWayById(paymentWayId);
	}

	@ApiOperation(value = "查询指定支付方式下的所有支付接口", notes = "查询指定支付方式下的所有支付接口")
	@RequestMapping(value = "/api/v1/paymentWays/{paymentWayId}/paymentinterfaces", method = GET)
	public ServiceResult<List<PaymentInterface>> paymentInterfaceList(@PathVariable Long paymentWayId) {
		return this.paymentInterfaceService.getPaymentInterfaceListByWayId(paymentWayId);
	}

}
