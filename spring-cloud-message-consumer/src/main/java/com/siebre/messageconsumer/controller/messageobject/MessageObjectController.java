package com.siebre.messageconsumer.controller.messageobject;

import com.siebre.basic.web.WebResult;
import com.siebre.messageconsumer.entity.MessageObject;
import com.siebre.messageconsumer.remoteservice.MessageObjectRemoteService;
import com.siebre.messageconsumer.service.messageobject.MessageObjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Api(tags = {"MessageObjectController"})
public class MessageObjectController {
	
	private Logger logger = LoggerFactory.getLogger(MessageObjectController.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MessageObjectService messageObjectService;
	
	@Autowired
	private MessageObjectRemoteService messageObjectRemoteService;
	
	@ApiOperation(value = "消息对象详情查询", notes = "测试用demo，获取消息对象的明细数据")
	@RequestMapping(path = "/api/v1/messageConsumer/{id}", method = { RequestMethod.GET })
	public WebResult<MessageObject> findById(@PathVariable Long id, HttpServletRequest request) {
//		logger.info("---------consumer call 1--------------");
		WebResult<MessageObject> webResult = this.messageObjectRemoteService.findById(1);
//		logger.info("---------consumer call 1 end--------------");
//		
//		logger.info("---------consumer call 2--------------");
//		String responseBody = this.restTemplate.getForObject("http://SPRING-CLOUD-MESSAGE-PROVIDER/api/v1/messageObject/" + id, String.class);
//		logger.info(responseBody);
//		logger.info("---------consumer call 2 end--------------");
		
//		ResponseEntity<String> responseEntity = (ResponseEntity) this.restTemplate.getForEntity("http://SPRING-CLOUD-UAA/api/v1/user/1", String.class);
//		String body = responseEntity.getBody();
		
		
		HttpSession session = request.getSession();
		session.getId();
		
		String hello = (String) session.getAttribute("hello");
		
		return WebResult.<MessageObject>builder().returnCode("200").build();
	}
	
	@RequestMapping(value = "/api/v1/messageObject", method = RequestMethod.POST)
	public WebResult<MessageObject> create(@RequestBody MessageObject messageObject) {
		this.messageObjectService.create(messageObject);
		return WebResult.<MessageObject>builder().returnCode("200").data(messageObject).build();
		
	}

}
