package org.example.databench.common.domain.file.datasource;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by shuangbofu on 2022/3/28 17:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JdbcParam extends DatasourceParam {
    private String jdbcUrl = "";
    private String username = "";
    private String password = "";
}
