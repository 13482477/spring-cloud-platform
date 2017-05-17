package com.siebre.payment.service.paymentchannel;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.payment.entity.paymentchannel.PaymentChannel;
import com.siebre.payment.mapper.paymentchannel.PaymentChannelMapper;

@Service
public class PaymentChannelService {

	private static Logger logger = LoggerFactory.getLogger(PaymentChannelService.class);

	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@Transactional
	public List<PaymentChannel> selectAll() {
		return paymentChannelMapper.selectAll();
	}
	
}
