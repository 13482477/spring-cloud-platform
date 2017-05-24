package com.siebre.payment.service.paymentway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.paymentinterface.PaymentInterface;
import com.siebre.payment.entity.paymentway.PaymentWay;
import com.siebre.payment.mapper.paymentinterface.PaymentInterfaceMapper;
import com.siebre.payment.mapper.paymentway.PaymentWayMapper;

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

	public ServiceResult<PaymentWay> getPaymentWayByCode(String paymentWayCode) {
		PaymentWay data = this.paymentWayMapper.getPaymentWayByCode(paymentWayCode);
		return ServiceResult.<PaymentWay>builder().success(true).data(data).build();
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
	public ServiceResult<PaymentWay> createPaymentWayAndPaymentInterface(PaymentWay paymentWay) {
		this.paymentWayMapper.insert(paymentWay);
		
		for (PaymentInterface paymentInterface : paymentWay.getPaymentInterfaces()) {
			paymentInterface.setPaymentWayId(paymentWay.getId());
			this.paymentInterfaceMapper.insert(paymentInterface);
		}
		return ServiceResult.<PaymentWay>builder().success(true).data(paymentWay).build();
	}
	
}
