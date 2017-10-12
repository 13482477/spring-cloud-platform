package com.siebre.uaa.openapi.controller;

import com.siebre.uaa.openapi.service.OpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jhonelee on 2017/7/21.
 */
@RestController
public class OpenApiController {

    @Autowired
    private OpenApiService openApiService;



}
