//package org.example.databench.common.deserialize;
//
//import org.example.databench.common.domain.file.FileCfg;
//import org.example.databench.common.domain.node.NodeCfg;
//
///**
// * Created by shuangbofu on 2021/9/20 11:39 下午
// */
//public class FileCfgDeserializer extends AbstractJsonDeserializer<FileCfg> {
//    @Override
//    String getTypeName() {
//        return "cfgType";
//    }
//
//    @Override
//    Class<? extends FileCfg> getClass(String type) {
//        switch (type) {
//            case "empty":
//                return FileCfg.EmptyFiLeCfg.class;
//            case "node":
//                return NodeCfg.class;
//            default:
//                throw new RuntimeException("Not supported");
//        }
//    }
//}
