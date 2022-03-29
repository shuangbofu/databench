package org.example.databench.executor.service;

import com.dtstack.dtcenter.loader.client.IClient;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.file.datasource.DatasourceParam;
import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.common.enums.DatasourceType;
import org.example.databench.executor.api.DatasourceApi;
import org.example.databench.executor.utils.DatasourceUtils;

/**
 * Created by shuangbofu on 2022/3/30 01:07
 */
public class DatasourceService implements DatasourceApi {

    @Override
    public boolean checkConnection(DatasourceCfg datasourceCfg, DatasourceType datasourceType) {
        return DatasourceUtils.checkConnection(getDatasourceParam(datasourceCfg), datasourceType);
    }

    @Override
    public QueryResult queryResult(DatasourceCfg datasourceCfg, DatasourceType datasourceType, String code) {
        SqlQueryDTO query = SqlQueryDTO.builder().sql(code).build();
        ISourceDTO datasource = DatasourceUtils.getDatasource(getDatasourceParam(datasourceCfg), datasourceType);
        IClient client = DatasourceUtils.getDatasourceClient(datasourceType);
        return new QueryResult(client.executeQuery(datasource, query));
    }

    private DatasourceParam getDatasourceParam(DatasourceCfg cfg) {
        return cfg.getParam();
    }
}
