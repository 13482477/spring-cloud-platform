package com.siebre.basic.utils;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass) {
        Object result = null;
        try {
            result = objectMapper.readValue(jsonAsString, pojoClass);
        } catch (Exception e) {
            logger.error("JSON[{}]反序列化失败", jsonAsString);
        }
        return result;
    }

    public static String toJson(Object pojo, boolean prettyPrint) {
        String result = null;
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            objectMapper.writeValue(jg, pojo);
            result = sw.toString();
        } catch (Exception e) {
            logger.error("[{}]序列化成JSON失败", pojo);
        }
        return result;
    }

    public static String mapToJson(Object map) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            logger.error("[{}]序列化成JSON失败", map);
        }
        return result;
    }

    public static Map<String, Object> jsonToMap(String jsonStr){
        Map<String, Object> map = new HashMap<>();

        try {
            ObjectMapper mapper = new ObjectMapper();

            map = mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});

            return map;

        } catch (IOException e) {
            logger.error("{}反序列化成Map失败",jsonStr);
        }
        return map;
    }
}
