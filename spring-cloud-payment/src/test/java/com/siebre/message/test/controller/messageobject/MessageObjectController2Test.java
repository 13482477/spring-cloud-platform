package com.siebre.message.test.controller.messageobject;

import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.siebre.message.test.base.MockitoTestConfig;
import com.siebre.payment.controller.messageobject.MessageObjectController;
import com.siebre.payment.entity.MessageObject;
import com.siebre.payment.service.messageobject.MessageObjectService;

public class MessageObjectController2Test extends MockitoTestConfig {
	
	private MockMvc mockMvc;
	
	@Mock
	private MessageObjectService messageObjectService;

	@InjectMocks
	private MessageObjectController messageObjectController;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(messageObjectController)
                .build();
	}
	
	@Test
	public void listTest() throws Exception {
		
		when(messageObjectService.get((long) 22))
		.then(new Answer<MessageObject>() {
			public MessageObject answer(InvocationOnMock invocation) throws Throwable {
				MessageObject messageObject = new MessageObject();
				messageObject.setId((long) 22);
				messageObject.setDescription("测试用数据");
				messageObject.setCurrentDate(new Date());
				return messageObject;
			}
		});
		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/messageObject/22")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		result.getResponse().getContentAsString();
	}

}
