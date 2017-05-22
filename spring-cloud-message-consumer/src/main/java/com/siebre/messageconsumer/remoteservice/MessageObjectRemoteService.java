package com.siebre.messageconsumer.remoteservice;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.siebre.basic.web.WebResult;
import com.siebre.messageconsumer.entity.MessageObject;

@FeignClient(value = "SPRING-CLOUD-MESSAGE-PROVIDER")
public interface MessageObjectRemoteService {
	
	@RequestMapping(value = "/api/v1/messageObject/list", method = RequestMethod.GET)
	public WebResult<List<MessageObject>> find(
			@RequestParam("page") int page, 
			@RequestParam("limit") int limit, 
			@RequestParam("sortField") String sortField, 
			@RequestParam("order") String order
			);
	
	@RequestMapping(value = "/api/v1/messageObject/{id}", method = RequestMethod.GET)
	public WebResult<MessageObject> findById(
			@RequestParam("id") long id 
			);

}
