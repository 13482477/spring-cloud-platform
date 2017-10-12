package com.siebre.security.authentication;

import com.siebre.security.userdetails.UserRemoteServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by jhonelee on 2017/7/20.
 */
@FeignClient(value = "SPRING-CLOUD-UAA", fallback = UserRemoteServiceFallback.class)
public interface OpenApiRemoteService {




}
