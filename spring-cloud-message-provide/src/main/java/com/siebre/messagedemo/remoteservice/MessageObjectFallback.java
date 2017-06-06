package com.siebre.messagedemo.remoteservice;

import com.siebre.basic.web.WebResult;
import com.siebre.messagedemo.entity.MessageObject;

import java.util.List;

public class MessageObjectFallback implements MessageObjectRemoteService {

	@Override
	public WebResult<List<MessageObject>> find(int page, int limit, String sortField, String order) {
		return WebResult.<List<MessageObject>>builder().returnCode("500").returnMessage("服务调用失败").build();
	}
	

}
