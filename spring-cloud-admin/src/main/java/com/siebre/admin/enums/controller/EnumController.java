package com.siebre.admin.enums.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.siebre.basic.cache.RedisCacheService;
import com.siebre.basic.enumutil.BaseEnum;
import com.siebre.basic.web.WebResult;

/**
 * Created by AdamTang on 2017/4/13.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Controller
public class EnumController {

    @Autowired
    private RedisCacheService redisCacheService;

    @RequestMapping(value = "/api/v1/enum/{key}", method = {RequestMethod.GET})
    public WebResult<List<BaseEnum>> getEnumByClassName(@PathVariable String key){
    	List<BaseEnum> data = (List<BaseEnum>) this.redisCacheService.get(key);
        return WebResult.<List<BaseEnum>>builder().returnCode("200").data(data).build();
    }
}
