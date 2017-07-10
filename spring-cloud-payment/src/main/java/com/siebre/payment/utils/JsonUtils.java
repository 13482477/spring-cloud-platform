package com.siebre.payment.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;

public class JsonUtils {
	protected static final Log logger = LogFactory.getLog(JsonUtils.class);

	public final static ObjectMapper MAPPER = new ObjectMapper();

	public JsonUtils(Inclusion inclusion) {
		MAPPER.getSerializationConfig().setSerializationInclusion(inclusion);
		MAPPER.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static <T> T getObject(String context, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		return (T) MAPPER.readValue(context, clazz);
	}

	public static JsonUtils buildNormalMapper() {
		return new JsonUtils(Inclusion.ALWAYS);
	}

	public static JsonUtils buildNonNullMapper() {
		return new JsonUtils(Inclusion.NON_NULL);
	}

	public static JsonUtils buildNonDefaultMapper() {
		return new JsonUtils(Inclusion.NON_DEFAULT);
	}

	public static JsonNode getJsonNode(String context) throws JsonParseException, JsonMappingException, IOException {
		return getObject(context, JsonNode.class);
	}

	public static JsonNode getJsonNode(String context, String fieldName) throws JsonParseException, JsonMappingException, IOException {
		return getObject(context, JsonNode.class).get(fieldName);
	}

	public static ObjectNode getObjectNode(String context) throws JsonParseException, JsonMappingException, IOException {
		return getObject(context, ObjectNode.class);
	}

	public static ArrayNode getArrayNode(String context) throws JsonParseException, JsonMappingException, IOException {
		return MAPPER.readValue(context, ArrayNode.class);
	}

	public static String getStringValue(JsonNode jsonNode, String fieldName) {
		return jsonNode.get(fieldName).getTextValue();
	}

	public static long getLongValue(JsonNode jsonNode, String fieldName) {
		return jsonNode.get(fieldName).getLongValue();
	}

	public static String getTextValue(String context, String fieldName) throws JsonParseException, JsonMappingException, IOException {
		return getStringValue(getObject(context, JsonNode.class), fieldName);
	}

	public static long getLongValue(String context, String fieldName) throws JsonParseException, JsonMappingException, IOException {
		return getLongValue(getObject(context, JsonNode.class), fieldName);
	}

	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return MAPPER.readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	public String toJson(Object object) {

		try {
			return MAPPER.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	public static void put(ObjectNode objNode, String fieldName, Object value) {
		putAllowNull(objNode, fieldName, value, true);
	}

	public static void putAllowNull(ObjectNode objNode, String fieldName, Object value, boolean isAllowNull) {
		if (objNode == null)
			return;
		if (value != null) {
			objNode.put(fieldName, value.toString());
		} else if (isAllowNull)
			objNode.put(fieldName, StringUtils.EMPTY);
	}

	public ObjectMapper getMapper() {
		return MAPPER;
	}

	public static ObjectMapper getObjectMapperInstance() {
		return MAPPER;
	}

}
