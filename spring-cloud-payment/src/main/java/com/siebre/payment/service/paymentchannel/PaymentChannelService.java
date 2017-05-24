package com.siebre.payment.service.paymentchannel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.paymentchannel.PaymentChannel;
import com.siebre.payment.mapper.paymentchannel.PaymentChannelMapper;

@Service
public class PaymentChannelService {

	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@Transactional("db")
	public ServiceResult<PaymentChannel> create(PaymentChannel paymentChannel) {
		this.paymentChannelMapper.insert(paymentChannel);
		return ServiceResult.<PaymentChannel>builder().success(Boolean.TRUE).data(paymentChannel).message(ServiceResult.SUCCESS_MESSAGE).build();
	}
	
	public ServiceResult<List<PaymentChannel>> searchAll() {
		List<PaymentChannel> data = this.paymentChannelMapper.selectAll();
		return ServiceResult.<List<PaymentChannel>>builder().success(Boolean.TRUE).data(data).message(ServiceResult.SUCCESS_MESSAGE).build();
	}
	
	public ServiceResult<List<PaymentChannel>> searchAllByPage(PageInfo pageInfo) {
		List<PaymentChannel> data = this.paymentChannelMapper.selectAllByPage(pageInfo);
		return ServiceResult.<List<PaymentChannel>>builder().success(Boolean.TRUE).data(data).message(ServiceResult.SUCCESS_MESSAGE).build();
	}

}
