package com.siebre.payment.paymentchannel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.basic.query.PageInfo;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;

@Service
public class PaymentChannelService {

	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@Transactional("db")
	public void create(PaymentChannel paymentChannel) {
		this.paymentChannelMapper.insert(paymentChannel);
	}
	
	public List<PaymentChannel> searchAll() {
		List<PaymentChannel> data = this.paymentChannelMapper.selectAll();
		return data;
	}
	
	public List<PaymentChannel> searchAllByPage(PageInfo pageInfo) {
		List<PaymentChannel> data = this.paymentChannelMapper.selectAllByPage(pageInfo);
		return data;
	}
	
	public PaymentChannel findById(Long id) {
		return this.paymentChannelMapper.selectByPrimaryKey(id);
	}

}
