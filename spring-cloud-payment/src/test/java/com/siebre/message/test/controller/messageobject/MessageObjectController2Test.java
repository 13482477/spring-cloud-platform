package com.siebre.message.test.controller.messageobject;

import com.siebre.message.test.base.MockitoTestConfig;

public class MessageObjectController2Test extends MockitoTestConfig {
	
//	private MockMvc mockMvc;
//	
//	@Mock
//	private MessageObjectService messageObjectService;
//
//	@InjectMocks
//	private MessageObjectController messageObjectController;
//	
//	@Before
//	public void init() {
//		MockitoAnnotations.initMocks(this);
//        this.mockMvc = MockMvcBuilders
//                .standaloneSetup(messageObjectController)
//                .build();
//	}
//	
//	@Test
//	public void listTest() throws Exception {
//		
//		when(messageObjectService.get((long) 22))
//		.then(new Answer<MessageObject>() {
//			public MessageObject answer(InvocationOnMock invocation) throws Throwable {
//				MessageObject messageObject = new MessageObject();
//				messageObject.setId((long) 22);
//				messageObject.setDescription("测试用数据");
//				messageObject.setCurrentDate(new Date());
//				return messageObject;
//			}
//		});
//		
//		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
//				.get("/api/v1/messageObject/22")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andReturn();
//		result.getResponse().getContentAsString();
//	}

}
