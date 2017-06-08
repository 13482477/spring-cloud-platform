package com.siebre.payment.paymentchannel.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymentchannel.vo.PaymentChannelVo;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class PaymentChannelController {

	@Autowired
	private PaymentChannelService paymentChannelService;

	@Autowired
	private PaymentWayService paymentWayService;

	@ApiOperation(value = "支付渠道列表", notes = "支付渠道列表")
	@RequestMapping(value = "/api/v1/paymentChannels", method = GET)
	public ServiceResult<List<PaymentChannelVo>> list() {
		return paymentChannelService.searchAll();
	}

	@ApiOperation(value = "创建支付渠道", notes = "创建支付渠道")
	@RequestMapping(value = "/api/v1/paymentChannels", method = POST)
	public ServiceResult<PaymentChannel> create(@RequestBody PaymentChannel paymentChannel) {
		return paymentChannelService.create(paymentChannel);
	}

	@ApiOperation(value = "根据主键值更新支付渠道", notes = "根据主键值更新支付渠道")
	@RequestMapping(value = "/api/v1/paymentChannels/{channelId}", method = PUT)
	public ServiceResult<PaymentChannel> update(@PathVariable Long channelId, @RequestBody PaymentChannel paymentChannel) {
		paymentChannel.setId(channelId);
		return paymentChannelService.updateById(paymentChannel);
	}

	@ApiOperation(value = "支付渠道详细信息", notes = "支付渠道详细信息")
	@RequestMapping(value = "/api/v1/paymentChannels/{channelId}", method = GET)
	public ServiceResult<PaymentChannel> detail(@PathVariable Long channelId) {
		return paymentChannelService.queryById(channelId);
	}

	@ApiOperation(value = "删除支付渠道", notes = "删除支付渠道")
	@RequestMapping(value = "/api/v1/paymentChannels/{channelId}", method = DELETE)
	public ServiceResult delete(@PathVariable Long channelId) {
		return paymentChannelService.deleteByid(channelId);
	}

	@ApiOperation(value = "获取该支付渠道下所有的支付方式", notes = "获取该支付渠道下所有的支付方式")
	@RequestMapping(value = "/api/v1/paymentChannels/{channelId}/paymentways", method = GET)
	public ServiceResult<List<PaymentWay>> paymentWayList(@PathVariable Long channelId) {
		List<PaymentWay> list = paymentWayService.getPaymentWayByChannelId(channelId);
		return ServiceResult.<List<PaymentWay>>builder().success(Boolean.TRUE).data(list).message(ServiceResult.SUCCESS_MESSAGE).build();
	}
}
