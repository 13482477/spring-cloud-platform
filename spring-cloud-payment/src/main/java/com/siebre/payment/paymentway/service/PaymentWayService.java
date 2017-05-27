package com.siebre.payment.paymentway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.mapper.PaymentInterfaceMapper;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;

@Service("paymentWayService")
public class PaymentWayService {
	
	@Autowired
	private PaymentWayMapper paymentWayMapper;
	
	@Autowired
	private PaymentInterfaceMapper paymentInterfaceMapper;

	public PaymentWay getPaymentWay(String paymentWayCode){
		return this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
	}

	public List<PaymentWay> getPaymentWayByChannelId(Long channelId){
		return this.paymentWayMapper.getPaymentWayByChannelId(channelId);
	}

	public PaymentWay getPaymentWayByCode(String paymentWayCode) {
		PaymentWay data = this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
		return data;
	}

	public PaymentInterface getPaymentInterface(String paymentWayCode, PaymentInterfaceType paymentInterfaceType) {
		PaymentWay paymentWay = this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
		for (PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
			if (paymentInterfaceType != null && paymentInterfaceType.equals(paymentInterface.getPaymentInterfaceType())) {
				return paymentInterface;
			}
		}
		return null;
	}

	public PaymentInterface getNotifyPaymentInterface(String notifyCode){
		return this.paymentInterfaceMapper.selectByCode(notifyCode);
	}
	
	@Transactional("db")
	public PaymentWay createPaymentWayAndPaymentInterface(PaymentWay paymentWay) {
		this.paymentWayMapper.insert(paymentWay);
		
		for (PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
			paymentInterface.setPaymentWayId(paymentWay.getId());
			this.paymentInterfaceMapper.insert(paymentInterface);
		}
		return paymentWay;
	}
	
}
