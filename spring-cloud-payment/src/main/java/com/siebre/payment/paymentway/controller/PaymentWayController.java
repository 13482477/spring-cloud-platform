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
@CrossOrigin("*")
public class PaymentWayController {

	@Autowired
	private PaymentWayService paymentWayService;

	@Autowired
	private PaymentInterfaceService paymentInterfaceService;
	
	/*@RequestMapping(value = "/api/v1/paymentWay/{paymentWayCode}", method = GET)
	public WebResult<PaymentWay> getPaymentWayByCode(@PathVariable String paymentWayCode) {
		PaymentWay data = this.paymentWayService.getPaymentWayByCode(paymentWayCode).getData();
		return WebResult.<PaymentWay>builder().returnCode(WebResult.SUCCESS_CODE).returnMessage("创建成功").data(data).build();
	}*/
	
	/*@RequestMapping(value = "/api/v1/paymentWay", method = POST)
	public WebResult<PaymentWay> createPaymentWayAndPaymentInterface(@RequestBody PaymentWay paymentWay) {
		PaymentWay data = this.paymentWayService.createPaymentWayAndPaymentInterface(paymentWay).getData();
		return WebResult.<PaymentWay>builder().returnCode(WebResult.SUCCESS_CODE).returnMessage("创建成功").data(data).build();
	}*/

	/**
	 * 根据分页信息查询支付方式
	 *
	 * @return
	 */
	@ApiOperation(value = "根据分页信息查询支付方式", notes = "根据分页信息查询支付方式")
	@RequestMapping(value = "/api/v1/paymentWays", method = GET)
	public ServiceResult<List<PaymentWay>> list(PageInfo pageInfo) {
		return this.paymentWayService.selectAllByPage(pageInfo);
	}

	/**
	 * 创建支付方式
	 *
	 * @param paymentWay
	 * @return
	 */
	@ApiOperation(value = "创建支付方式", notes = "创建支付方式")
	@RequestMapping(value = "/api/v1/paymentWays", method = POST)
	public ServiceResult<PaymentWay> create(@RequestBody PaymentWay paymentWay) {
		return paymentWayService.createPaymentWay(paymentWay);
	}

	/**
	 * 根据主键值更新支付方式
	 * @param paymentWay
	 * @return
	 */
	@ApiOperation(value = "根据主键值更新支付方式", notes = "根据主键值更新支付方式")
	@RequestMapping(value = "/api/v1/paymentWays", method = PUT)
	public ServiceResult<PaymentWay> update(@RequestBody PaymentWay paymentWay) {
		return paymentWayService.updatePaymentWay(paymentWay);
	}

	/**
	 * 查询详细
	 * @param id paymentWay的主键值
	 * @return
	 */
	@ApiOperation(value = "查询支付方式详细信息", notes = "查询支付方式详细信息")
	@RequestMapping(value = "/api/v1/paymentWays/{id}", method = GET)
	public ServiceResult<PaymentWay> detail(@PathVariable Long id) {
		return paymentWayService.getPaymentWayById(id);
	}

	/**
	 * 删除支付方式
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除支付方式", notes = "删除支付方式")
	@RequestMapping(value = "/api/v1/paymentWays/{id}", method = DELETE)
	public ServiceResult delete(@PathVariable Long id) {
		return paymentWayService.deletePaymentWayById(id);
	}

	/**
	 * 查询指定支付方式下的所有支付接口
	 * @param id 支付方式id
	 * @return
	 */
	@ApiOperation(value = "查询指定支付方式下的所有支付接口", notes = "查询指定支付方式下的所有支付接口")
	@RequestMapping(value = "/api/v1/paymentWays/{id}/paymentinterface", method = GET)
	public ServiceResult<List<PaymentInterface>> paymentInterfaceList(@PathVariable Long id) {
		return this.paymentInterfaceService.getPaymentInterfaceListByWayId(id);
	}

}
