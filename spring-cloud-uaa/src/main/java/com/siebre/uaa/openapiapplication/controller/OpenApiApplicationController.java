package com.siebre.uaa.openapiapplication.controller;

import com.siebre.basic.web.WebResult;
import com.siebre.uaa.openapi.entity.OpenApi;
import com.siebre.uaa.openapi.service.OpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jhonelee on 2017/7/21.
 */
@RestController
public class OpenApiApplicationController {

    @Autowired
    private OpenApiService openApiService;

    @RequestMapping(value = "/api/v1/openApi", method = RequestMethod.POST)
    public WebResult<OpenApi> createOpenApi(@RequestBody OpenApi openApi) {
        this.openApiService.createOpenApi(openApi);
        return WebResult.<OpenApi>builder().returnCode("200").returnMessage("创建成功").data(openApi).build();
    }


}
