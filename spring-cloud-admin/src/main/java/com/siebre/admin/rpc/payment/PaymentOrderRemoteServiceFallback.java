package com.siebre.admin.rpc.payment;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.siebre.basic.web.WebResult;

@Component
public class PaymentOrderRemoteServiceFallback implements PaymentOrderRemoteService {

	@Override
	public WebResult<List<PaymentOrder>> find(String channelCodeList, Integer showCount, Integer currentPage, String orderNumber, String refundNumber, Date startDate, Date endDate) {
		return WebResult.<List<PaymentOrder>>builder().returnCode("500").returnMessage("连接超时").build();
	}

}
