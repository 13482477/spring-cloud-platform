package com.siebre.message.test.service.messageobject;

import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.siebre.message.test.base.ServiceMockTestConfig;
import com.siebre.messagedemo.entity.MessageObject;
import com.siebre.messagedemo.mapper.MessageObjectMapper;
import com.siebre.messagedemo.service.messageobject.MessageObjectService;

public class MessageObjectServiceTest extends ServiceMockTestConfig{
	
	@Mock
	private MessageObjectMapper messageObjectMapper;
	
	@InjectMocks
	private MessageObjectService messageObjectService;
	
	@Test
	public void getTest() {
		when(this.messageObjectMapper.selectByPrimaryKey(new Long(1)))
		.thenAnswer(new Answer<MessageObject>() {
			public MessageObject answer(InvocationOnMock invocation) throws Throwable {
				MessageObject messageObject = new MessageObject();
				messageObject.setId((long) 100);
				messageObject.setCurrentDate(new Date());
				messageObject.setDescription("mock对象");
				return messageObject;
			}
		});
		
		MessageObject messageObject = this.messageObjectService.get((long) 1);
		Assert.assertTrue(messageObject.getId() == 100);
		Assert.assertTrue("mock对象".equals(messageObject.getDescription()));
	}
	

}
