package org.example.databench.common.domain.file;

import lombok.Data;
import org.example.databench.common.domain.file.datasource.DatasourceParam;

/**
 * Created by shuangbofu on 2022/3/28 14:58
 */
@Data
public class DatasourceCfg extends FileCfg {
    private DatasourceParam param;

    public DatasourceCfg() {
    }

    public DatasourceCfg(DatasourceParam param) {
        this.param = param;
    }
}
