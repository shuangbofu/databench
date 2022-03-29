package org.example.databench.common.domain.file;

import lombok.Data;

/**
 * Created by shuangbofu on 2022/3/28 13:56
 */
@Data
public class QueryCfg implements FileCfg {
    private Long datasourceFileId = 0L;
    private Integer datasourceFileVersion = 0;
}
