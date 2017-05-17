package com.siebre.messagedemo.controller.messageObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.messagedemo.entity.messageobject.MessageObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "MessageObjectController")
public class MessageObjectController {

	@ApiOperation(value = "消息对象列表查询", notes = "测试用demo，获取消息对象列表数据", httpMethod = "GET")
	@RequestMapping(path = "/api/v1/messageObject/list", method = { RequestMethod.GET })
	@ResponseBody
	public WebResult<List<MessageObject>> list(@RequestParam int page, @RequestParam int limit, @RequestParam String sortField, @RequestParam String order ) {

		PageInfo pageInfo = new PageInfo(limit, page, sortField, order);

		List<MessageObject> data = new ArrayList<MessageObject>();
		MessageObject messageObject1 = new MessageObject();
		messageObject1.setId(new Long(1));
		messageObject1.setCurrentDate(new Date());
		messageObject1.setDescription("Get message object success! Token is " + UUID.randomUUID());
		data.add(messageObject1);

		MessageObject messageObject2 = new MessageObject();
		messageObject2.setId(new Long(2));
		messageObject2.setCurrentDate(new Date());
		messageObject2.setDescription("Get message object success! Token is " + UUID.randomUUID());
		data.add(messageObject2);

		MessageObject messageObject3 = new MessageObject();
		messageObject3.setId(new Long(3));
		messageObject3.setCurrentDate(new Date());
		messageObject3.setDescription("Get message object success! Token is " + UUID.randomUUID());
		data.add(messageObject3);

		return WebResult.<List<MessageObject>>builder().returnCode("200").data(data).pageInfo(pageInfo).build();
	}

	@ApiOperation(value = "消息对象详情查询", notes = "测试用demo，获取消息对象的明细数据", httpMethod = "GET")
	@RequestMapping(path = "/api/v1/messageObject/{id}", method = { RequestMethod.GET })
	@ResponseBody
	public WebResult<MessageObject> get(@PathVariable Long id) {
		MessageObject messageObject = new MessageObject();
		messageObject.setId(id);
		messageObject.setCurrentDate(new Date());
		messageObject.setDescription("Get message object success! Token is " + UUID.randomUUID());
		return WebResult.<MessageObject>builder().returnCode("200").data(messageObject).build();
	}

	@ApiOperation(value = "新增消息对象", notes = "新增消息对象，在数据库中插入一条消息数据", httpMethod = "POST")
	@RequestMapping(path = "/api/v1/messageObject", method = { RequestMethod.POST })
	@ResponseBody
	public WebResult<MessageObject> post(@RequestBody MessageObject messageObject) {
		return WebResult.<MessageObject>builder().returnCode("200").data(messageObject).build();
	}

	@ApiOperation(value = "修改消息对象", notes = "修改消息对象，根据用户提供的消息对象id，修改消息对象", httpMethod = "POST")
	@RequestMapping(path = "/api/v1/messageObject/{id}", method = { RequestMethod.PUT })
	@ResponseBody
	public WebResult<MessageObject> put(@PathVariable Long id, @RequestBody MessageObject messageObject) {
		messageObject.setId(id);
		return WebResult.<MessageObject>builder().returnCode("200").data(messageObject).build();
	}

}
