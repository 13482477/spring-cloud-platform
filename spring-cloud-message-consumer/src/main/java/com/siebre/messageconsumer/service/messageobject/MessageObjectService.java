package com.siebre.messageconsumer.service.messageobject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.basic.query.PageInfo;
import com.siebre.messageconsumer.entity.MessageObject;
import com.siebre.messageconsumer.mapper.MessageObjectMapper;

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
