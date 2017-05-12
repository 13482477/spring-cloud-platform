package com.siebre.springcloud.userconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserConsumerController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/userConsumer", method = RequestMethod.GET)
	public String doConsumer() {
		return this.restTemplate.getForEntity("http://SPRING-CLOUD-USER/index", String.class).getBody();
	}

}
