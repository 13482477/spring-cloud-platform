package com.siebre.payment.remoteservice;

import java.util.List;

import com.siebre.basic.web.WebResult;
import com.siebre.payment.entity.MessageObject;

public class MessageObjectFallback implements MessageObjectRemoteService {

	@Override
	public WebResult<List<MessageObject>> find(int page, int limit, String sortField, String order) {
		return WebResult.<List<MessageObject>>builder().returnCode("500").returnMessage("服务调用失败").build();
	}
	

}
