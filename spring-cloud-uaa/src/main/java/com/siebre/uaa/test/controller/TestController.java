package com.siebre.uaa.test.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhonelee on 2017/7/21.
 */
@RestController
public class TestController {

    @RequestMapping(value = "/openApi/v1/openApiTest",method = RequestMethod.GET)
    public String openApiTest() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("apiName", "testOpenApi");
        result.put("redirectUrl", "http://www.baidu.com");

        return JSON.toJSONString(result);
    }

}
