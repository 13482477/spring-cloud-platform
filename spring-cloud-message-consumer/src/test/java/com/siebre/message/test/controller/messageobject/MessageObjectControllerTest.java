package com.siebre.message.test.controller.messageobject;

import com.siebre.message.test.base.WebApplicationMockConfig;
import com.siebre.messageconsumer.service.messageobject.MessageObjectService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class MessageObjectControllerTest extends WebApplicationMockConfig {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@InjectMocks
	private MessageObjectService messageObjectService;
	
	
	@Test
	public void listTest() throws Exception {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		MvcResult result = mvc.perform(MockMvcRequestBuilders
				.get("/api/v1/messageObject/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		result.getResponse().getContentAsString();
	}

}
