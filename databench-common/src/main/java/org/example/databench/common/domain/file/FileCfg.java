package org.example.databench.common.domain.file;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.databench.common.domain.node.NodeCfg;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY;

/**
 * Created by shuangbofu on 2021/9/11 2:46 下午
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = EXTERNAL_PROPERTY, property = "cfgType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NodeCfg.class, name = "node"),
        @JsonSubTypes.Type(value = FileCfg.EmptyFiLeCfg.class, name = "empty"),
        @JsonSubTypes.Type(value = QueryCfg.class, name = "query"),
        @JsonSubTypes.Type(value = ApiCfg.class, name = "aip"),
        @JsonSubTypes.Type(value = DatasourceCfg.class, name = "datasource"),
})
public interface FileCfg {

    class EmptyFiLeCfg implements FileCfg {
    }
}
