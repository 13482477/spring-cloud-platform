package com.siebre.messagedemo.controller.messageobject;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.messagedemo.entity.MessageObject;
import com.siebre.messagedemo.service.messageobject.MessageObjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = {"MessageObjectController"})
public class MessageObjectController {
	
	private Logger logger = LoggerFactory.getLogger(MessageObjectController.class);
	
	@Autowired
	private MessageObjectService messageObjectService;
	
	@ApiOperation(value = "消息对象列表查询", notes = "测试用demo，获取消息对象列表数据")
	@ApiImplicitParams(value = {
					@ApiImplicitParam(name = "page", value = "分页参数，当前页", paramType = "query", dataType = "int"),
					@ApiImplicitParam(name = "limit", value = "显示记录数", paramType = "query", dataType = "int"),
					@ApiImplicitParam(name = "sortField", value = "排序字段", paramType = "query", dataType = "string"),
					@ApiImplicitParam(name = "order", value = "升序或降序", paramType = "query", dataType = "string", allowableValues = "asc,desc"),
				}
			)
	@RequestMapping(path = "/api/v1/messageObject/list", method = { RequestMethod.GET })
	@ResponseBody
	public WebResult<List<MessageObject>> find(@RequestParam int page, @RequestParam int limit, @RequestParam String sortField, @RequestParam String order) {
		PageInfo pageInfo = new PageInfo(limit, page, sortField, order);
		List<MessageObject> messageObjects = this.messageObjectService.find(pageInfo);
		return WebResult.<List<MessageObject>>builder().returnCode("200").data(messageObjects).pageInfo(pageInfo).build();
	}

	@ApiOperation(value = "消息对象详情查询", notes = "测试用demo，获取消息对象的明细数据")
	@RequestMapping(path = "/api/v1/messageObject/{id}", method = { RequestMethod.GET })
	@ResponseBody
	public WebResult<MessageObject> findById(@PathVariable Long id) {
		logger.info("--------call find by id---------");
		MessageObject messageObject = this.messageObjectService.get(id);
		return WebResult.<MessageObject>builder().returnCode("200").data(messageObject).returnMessage("成功获取数据").build();
	}

	@ApiOperation(value = "新增消息对象", notes = "新增消息对象，在数据库中插入一条消息数据")
	@RequestMapping(path = "/api/v1/messageObject", method = { RequestMethod.POST })
	@ResponseBody
	public WebResult<MessageObject> create(@RequestBody MessageObject messageObject) {
		this.messageObjectService.create(messageObject);
		return WebResult.<MessageObject>builder().returnCode("200").data(messageObject).returnMessage("创建成功").build();
	}

	@ApiOperation(value = "编辑消息对象", notes = "根据客户端提交的消息对象id及消息对象数据，更新消息对象")
	@RequestMapping(path = "/api/v1/messageObject/{id}", method = { RequestMethod.PUT })
	@ResponseBody
	public WebResult<MessageObject> update(@PathVariable Long id, @RequestBody MessageObject messageObject) {
		messageObject.setId(id);
		this.messageObjectService.update(messageObject);
		return WebResult.<MessageObject>builder().returnCode("200").data(messageObject).returnMessage("修改成功").build();
	}
	
	@ApiOperation(value = "删除消息对象", notes = "根据客户端提交的消息对象id，删除消息对象")
	@RequestMapping(path = "/api/v1/messageObject/{id}", method = { RequestMethod.DELETE })
	@ResponseBody
	public WebResult<MessageObject> delete(@PathVariable Long id) {
		this.messageObjectService.delete(id);
		return WebResult.<MessageObject>builder().returnCode("200").returnMessage("删除成功").build();
	}

}
