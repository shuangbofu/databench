package org.example.databench.executor.api;

import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.common.enums.DatasourceType;

/**
 * Created by shuangbofu on 2022/3/30 00:46
 */
public interface DatasourceApi extends JobApi {

    boolean checkConnection(DatasourceCfg datasourceCfg, DatasourceType datasourceType);

    QueryResult fetchResult(String jobId);
}
