package org.example.databench.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class JSONUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

    static {
        setupMapper(MAPPER);
    }

    public static ObjectMapper setupMapper(ObjectMapper mapper) {
        return mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

    public static void setUpMapper(Consumer<ObjectMapper> consumer) {
        consumer.accept(MAPPER);
    }

    @SafeVarargs
    public static void setUpMapper(Consumer<ObjectMapper>... consumers) {
        Arrays.stream(consumers).forEach(JSONUtils::setUpMapper);
    }

    public static <T> T parseObject(String json, Class<T> tClass) {
        try {
            return MAPPER.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Parse json error", e);
        }
    }

    public static <T> T parseObject(Object fromValue, Class<T> tClass) {
        return MAPPER.convertValue(fromValue, tClass);
    }

    public static JsonNode object2JsonNode(Object fromValue) {
        return parseObject(fromValue, JsonNode.class);
    }

    public static JsonNode jsonString2JsonNode(String jsonString) throws JsonProcessingException {
        return parseObject(jsonString, JsonNode.class);
    }

    public static Map<String, String> jsonString2Map(String jsonString) throws JsonProcessingException {
        return MAPPER.convertValue(parseObject(jsonString, JsonNode.class), new TypeReference<>() {
        });
    }

    public static Map<String, Object> jsonNode2Map(JsonNode node) {
        return MAPPER.convertValue(node, new TypeReference<>() {
        });
    }

    public static <T> List<T> parseArray(String json, Class<T> tClass) {
        try {
            return MAPPER.readValue(json, getJavaType(tClass));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("parse json 2 array error");
        }
    }

    public static <T> List<T> parseArray(Object fromValue, Class<T> tClass) {
        return MAPPER.convertValue(fromValue, getJavaType(tClass));
    }

    public static String toJSONString(Object object, Boolean... formatting) {
        try {
            if (formatting != null && formatting.length > 0 && formatting[0]) {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            }
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("bean to json error", e);
        }
    }

    private static <T> JavaType getJavaType(Class<T> tClass) {
        return MAPPER.getTypeFactory()
                .constructCollectionType(List.class, tClass);
    }
}
