package com.siebre.security.metadata;

import java.util.List;

import org.springframework.stereotype.Component;

import com.siebre.basic.web.WebResult;
import com.siebre.security.entity.Resource;

@Component
public class ResourceRemoteServiceFallback implements ResourceRemoteService {

	@Override
	public WebResult<List<Resource>> allResourcesAndPermission() {
		return WebResult.<List<Resource>>builder().returnCode("500").returnMessage("服务调用失败").build();
	}

}
