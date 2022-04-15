package org.example.executor.datasourceX;

import com.dtstack.dtcenter.loader.client.IClient;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import org.example.databench.common.enums.DatasourceType;
import org.example.executor.api.ExecutableApi;
import org.example.executor.api.domain.ApiParam;
import org.example.executor.api.domain.query.QueryResult;
import org.example.executor.base.service.AbstractLocalExecutor;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2022/3/30 01:07
 */
public class DatasourceXExecutor extends AbstractLocalExecutor implements ExecutableApi {

    @Override
    public boolean checkConnection(ApiParam param) {
        return DatasourceUtils.checkConnection(param.getJdbcParam(), param.getDatasourceType());
    }

    @Override
    public QueryResult fetchResult(String jobId) {
        return readFile(jobId, "json", QueryResult.class).orElse(new QueryResult());
    }

    @Override
    protected void execute(ApiParam param, JobLogger logger) {
        String sql = param.getSql();
        DatasourceType datasourceType = param.getDatasourceType();
        SqlQueryDTO query = SqlQueryDTO.builder().sql(sql).build();
        ISourceDTO datasource = DatasourceUtils.getDatasource(param.getJdbcParam(), datasourceType);
        IClient client = DatasourceUtils.getDatasourceClient(datasourceType);
        logger.info("开始执行SQL:" + sql);
        List<Map<String, Object>> result = client.executeQuery(datasource, query);
        saveFile(new QueryResult(result), "json");
    }

}
