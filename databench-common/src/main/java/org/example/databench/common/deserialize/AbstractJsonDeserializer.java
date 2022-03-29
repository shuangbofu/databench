//package org.example.databench.common.deserialize;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//
//import java.io.IOException;
//
//public abstract class AbstractJsonDeserializer<T> extends JsonDeserializer<T> {
//
//    @Override
//    public T deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
//        jp.getCodec();
//        T result = null;
//        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
//        ObjectNode root = mapper.readTree(jp);
//        String typeName = getTypeName();
//        if (root.has(typeName)) {
//            String filterType = root.get(typeName).asText();
//            Class<? extends T> clazz = getClass(filterType);
//            result = mapper.readValue(root.toString(), clazz);
//        }
//        return result;
//    }
//
//    abstract String getTypeName();
//
//    abstract Class<? extends T> getClass(String type);
//}
