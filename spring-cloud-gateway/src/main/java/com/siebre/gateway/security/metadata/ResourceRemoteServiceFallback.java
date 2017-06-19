package com.siebre.gateway.security.metadata;

import java.util.List;

import org.springframework.stereotype.Component;

import com.siebre.basic.web.WebResult;
import com.siebre.gateway.security.entity.Resource;

@Component
public class ResourceRemoteServiceFallback implements ResourceRemoteService {

	@Override
	public WebResult<List<Resource>> allResourcesAndPermission() {
		return WebResult.<List<Resource>>builder().returnCode("500").returnMessage("请求超时").build();
	}

}
