package com.siebre.payment.serviceinterface.paymentchannel;

import java.util.List;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.paymentchannel.PaymentChannel;

public interface IPaymentChannelService {
	
	public ServiceResult<PaymentChannel> create(PaymentChannel paymentChannel);
	
	public ServiceResult<List<PaymentChannel>> searchAll();
	
	public ServiceResult<List<PaymentChannel>> searchAllByPage(PageInfo pageInfo);

}
