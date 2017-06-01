package com.siebre.payment.paymentchannel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;

@RestController
public class PaymentChannelController {
	
	@Autowired
	private PaymentChannelService paymentChannelService;
	
	@RequestMapping(value = "/api/v1/paymentChannel", method = {RequestMethod.POST})
	public PaymentChannel create(@RequestBody PaymentChannel paymentChannel) {
		this.paymentChannelService.create(paymentChannel);
		return paymentChannel;
	}
	
	@RequestMapping(value = "/api/v1/paymentChannel/{id}", method = {RequestMethod.GET})
	public PaymentChannel get(@PathVariable long id) {
		PaymentChannel paymentChannel = this.paymentChannelService.findById(id);
		return paymentChannel;
	}
	
	@RequestMapping(value = "/api/v1/paymentChannels", method = {RequestMethod.GET})
	public WebResult<List<PaymentChannel>> find(@RequestParam int page, @RequestParam int limit, @RequestParam String sortField, @RequestParam String order) {
		PageInfo pageInfo = new PageInfo(limit, page, sortField, order);
		List<PaymentChannel> paymentChannels = this.paymentChannelService.searchAllByPage(pageInfo);
		return WebResult.<List<PaymentChannel>>builder().data(paymentChannels).pageInfo(pageInfo).build();
	}

}
