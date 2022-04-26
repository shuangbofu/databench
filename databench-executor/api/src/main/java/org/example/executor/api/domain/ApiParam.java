package org.example.executor.api.domain;

import lombok.Data;
import org.example.databench.common.domain.file.datasource.JdbcParam;
import org.example.databench.common.enums.DatasourceType;

/**
 * Created by shuangbofu on 2022/4/15 15:01
 */
@Data
public class ApiParam {
    private String name;
    private String description;
    private String executorType;
    private String type;
    private String code;
    private String sql;
    private JdbcParam jdbcParam;
    private DatasourceType datasourceType;
    private String tenant;
}
