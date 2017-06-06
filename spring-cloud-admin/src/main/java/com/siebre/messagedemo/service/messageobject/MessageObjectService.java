package com.siebre.messagedemo.service.messageobject;

import com.siebre.basic.query.PageInfo;
import com.siebre.messagedemo.entity.MessageObject;
import com.siebre.messagedemo.mapper.MessageObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageObjectService {

	@Autowired
	private MessageObjectMapper messageObjectMapper;

	public void create(MessageObject messageObject) {
		this.messageObjectMapper.insert(messageObject);
	}
	
	public void update(MessageObject messageObject) {
		this.messageObjectMapper.updateByPrimaryKey(messageObject);
	}
	
	public void updateChange(MessageObject messageObject) {
		this.messageObjectMapper.updateByPrimaryKeySelective(messageObject);
	}

	public List<MessageObject> find(PageInfo pageInfo) {
		return this.messageObjectMapper.selectByQuery(pageInfo);
	}
	
	public void delete(Long id) {
		this.messageObjectMapper.deleteByPrimaryKey(id);
	}
	
	public MessageObject get(Long id) {
		return this.messageObjectMapper.selectByPrimaryKey(id);
	}

}
