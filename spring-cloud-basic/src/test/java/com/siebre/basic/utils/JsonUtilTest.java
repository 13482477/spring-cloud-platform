package com.siebre.basic.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;


import static org.junit.Assert.*;

/**
 * Created by AdamTang on 2017/4/24.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class JsonUtilTest {
    private Logger logger = LoggerFactory.getLogger(JsonUtilTest.class);
    @Test
    public void mapToJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"response\":{\"code\":\"200\",\"msg\":\"service\",\"sub_code\":\"isp.unknow-error\",\"sub_msg\":\"系统繁忙\"},\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\"}";
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> subMap = new HashMap<>();
        subMap.put("code","200");
        subMap.put("msg","service");
        subMap.put("sub_code","isp.unknow-error");
        subMap.put("sub_msg","系统繁忙");
        map.put("response",subMap);
        map.put("sign","ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE");

        String resultJson =  objectMapper.writeValueAsString(map);

        System.out.println(resultJson);


    }

    @Test
    public void jsonToMap() throws Exception {
        String json = "{\"response\":{\"code\":\"200\",\"msg\":\"service\",\"sub_code\":\"isp.unknow-error\",\"sub_msg\":\"系统繁忙\"},\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\"}";
        Map<String,Object> result = JsonUtil.jsonToMap(json);
        System.out.println(result.get("sign"));
        assertTrue("ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE".equals(result.get("sign")));
    }

}