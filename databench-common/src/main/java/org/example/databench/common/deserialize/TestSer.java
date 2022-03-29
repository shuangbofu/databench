//package org.example.databench.common.deserialize;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import org.example.databench.common.domain.file.FileCfg;
//import org.example.databench.common.utils.JSONUtils;
//
//import java.io.IOException;
//
///**
// * Created by shuangbofu on 2021/9/20 11:49 下午
// */
//public class TestSer extends JsonSerializer<FileCfg> {
//    @Override
//    public void serialize(FileCfg cfg, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        jsonGenerator.writeString(JSONUtils.toJSONString(cfg));
//    }
//}
