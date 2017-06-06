package com.siebre.payment.paymentchannel.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@CrossOrigin("*")
public class PaymentChannelController {

	@Autowired
	private PaymentChannelService paymentChannelService;

	@Autowired
	private PaymentWayService paymentWayService;

	/**
	 * 创建支付渠道
	 *
	 * @param paymentChannel
	 * @return
	 */
	@ApiOperation(value = "创建支付渠道", notes = "创建支付渠道")
	@RequestMapping(value = "/api/v1/paymentChannel", method = POST)
	public PaymentChannel create(@RequestBody PaymentChannel paymentChannel) {
		this.paymentChannelService.create(paymentChannel);
		return paymentChannel;
	}

	/**
	 * 支付渠道详细信息
	 *
	 * @param id 支付渠道id
	 * @return
	 */
	@ApiOperation(value = "支付渠道详细信息", notes = "支付渠道详细信息")
	@RequestMapping(value = "/api/v1/paymentChannel/{id}", method = GET)
	public PaymentChannel get(@PathVariable long id) {
		PaymentChannel paymentChannel = this.paymentChannelService.findById(id);
		return paymentChannel;
	}

	/**
	 * 支付渠道列表
	 *
	 * @return 支付渠道列表
	 */
	@ApiOperation(value = "支付渠道列表", notes = "支付渠道列表")
	@RequestMapping(value = "/api/v1/paymentChannels", method = GET)
	public WebResult<List<PaymentChannel>> find(@RequestParam int page, @RequestParam int limit, @RequestParam String sortField, @RequestParam String order) {
		PageInfo pageInfo = new PageInfo(limit, page, sortField, order);
		List<PaymentChannel> paymentChannels = this.paymentChannelService.searchAllByPage(pageInfo);
		return WebResult.<List<PaymentChannel>>builder().data(paymentChannels).pageInfo(pageInfo).build();
	}

	/**
	 * 根据主键值更新支付渠道
	 *
	 * @return
	 */
	@ApiOperation(value = "根据主键值更新支付渠道", notes = "根据主键值更新支付渠道")
	@RequestMapping(method = PUT)
	public ServiceResult<PaymentChannel> update(@RequestBody PaymentChannel paymentChannel) {
		return paymentChannelService.updateById(paymentChannel);
	}

	/**
	 * 删除支付渠道
	 *
	 * @param id 支付渠道id
	 * @return
	 */
	@ApiOperation(value = "删除支付渠道", notes = "删除支付渠道")
	@RequestMapping(value = "/{id}", method = DELETE)
	public ServiceResult delete(@PathVariable Long id) {
		return paymentChannelService.deleteByid(id);
	}

	/**
	 * 获取该支付渠道下所有的支付方式
	 *
	 * @param id 支付渠道id
	 * @return
	 */
	@ApiOperation(value = "获取该支付渠道下所有的支付方式", notes = "获取该支付渠道下所有的支付方式")
	@RequestMapping(value = "/{id}/paymentway", method = GET)
	public ServiceResult<List<PaymentWay>> paymentWayList(@PathVariable Long id) {
		List<PaymentWay> list = paymentWayService.getPaymentWayByChannelId(id);
		return ServiceResult.<List<PaymentWay>>builder().success(Boolean.TRUE).data(list).message(ServiceResult.SUCCESS_MESSAGE).build();
	}
}
