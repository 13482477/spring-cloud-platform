package com.siebre.messageconsumer.controller.messageobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.siebre.basic.web.WebResult;
import com.siebre.messageconsumer.entity.MessageObject;
import com.siebre.messageconsumer.remoteservice.MessageObjectRemoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = {"MessageObjectController"})
public class MessageObjectController {
	
	private Logger logger = LoggerFactory.getLogger(MessageObjectController.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MessageObjectRemoteService messageObjectRemoteService;
	
	@ApiOperation(value = "消息对象详情查询", notes = "测试用demo，获取消息对象的明细数据")
	@RequestMapping(path = "/api/v1/messageConsumer/{id}", method = { RequestMethod.GET })
	@ResponseBody
	public WebResult<MessageObject> findById(@PathVariable Long id) {
		logger.info("---------consumer call 1--------------");
		WebResult<MessageObject> webResult = this.messageObjectRemoteService.findById(1);
		logger.info("---------consumer call 1 end--------------");
		
		logger.info("---------consumer call 2--------------");
		String responseBody = this.restTemplate.getForObject("http://SPRING-CLOUD-MESSAGE-PROVIDER/api/v1/messageObject/" + id, String.class);
		logger.info(responseBody);
		logger.info("---------consumer call 2 end--------------");
		return webResult;
	}

}
