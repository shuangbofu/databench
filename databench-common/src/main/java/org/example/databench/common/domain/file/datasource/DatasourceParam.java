package org.example.databench.common.domain.file.datasource;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY;

/**
 * Created by shuangbofu on 2022/3/28 17:14
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = EXTERNAL_PROPERTY, property = "paramType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JdbcParam.class, name = "jdbc"),
})
public class DatasourceParam {
}
