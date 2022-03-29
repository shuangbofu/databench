//package org.example.databench.common.deserialize;
//
//import org.example.databench.common.domain.file.FileContent;
//import org.example.databench.common.domain.node.NodeContent;
//import org.example.databench.common.domain.resource.FunctionContent;
//import org.example.databench.common.domain.resource.ResourceContent;
//
///**
// * Created by shuangbofu on 2021/9/20 11:40 下午
// */
//public class FileContentDeserializer extends AbstractJsonDeserializer<FileContent> {
//    @Override
//    String getTypeName() {
//        return "contentType";
//    }
//
//    @Override
//    Class<? extends FileContent> getClass(String type) {
//        /*
//            @JsonSubTypes.Type(value = NodeContent.class, name = "node"),
//        @JsonSubTypes.Type(value = FileContent.class, name = "default"),
//        @JsonSubTypes.Type(value = ResourceContent.class, name = "resource"),
//        @JsonSubTypes.Type(value = FunctionContent.class, name = "function")
//         */
//        switch (type) {
//            case "node":
//                return NodeContent.class;
//            case "default":
//                return FileContent.class;
//            case "resource":
//                return ResourceContent.class;
//            case "function":
//                return FunctionContent.class;
//            default:
//                throw new RuntimeException("Not supported");
//        }
//    }
//}
