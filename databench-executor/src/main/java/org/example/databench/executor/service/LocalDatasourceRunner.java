package org.example.databench.executor.service;

import com.dtstack.dtcenter.loader.client.IClient;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.domain.file.datasource.DatasourceParam;
import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.common.enums.DatasourceType;
import org.example.databench.common.enums.FileType;
import org.example.databench.executor.api.DatasourceApi;
import org.example.databench.executor.utils.DatasourceUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2022/3/30 01:07
 */
public class LocalDatasourceRunner extends AbstractJobRunner implements DatasourceApi {

    @Override
    public boolean checkConnection(DatasourceCfg datasourceCfg, DatasourceType datasourceType) {
        return DatasourceUtils.checkConnection(getDatasourceParam(datasourceCfg), datasourceType);
    }

    @Override
    public QueryResult fetchResult(String jobId) {
        return readFile(jobId, "json", QueryResult.class).orElse(new QueryResult());
    }

    @Override
    protected void execute(FileType fileType, FileContent fileContent,
                           FileCfg fileCfg, JobLogger logger) throws Exception {
        String sql = fileContent.getCode();
        DatasourceType datasourceType = fileType.toDsType();
        SqlQueryDTO query = SqlQueryDTO.builder().sql(sql).build();
        ISourceDTO datasource = DatasourceUtils.getDatasource(getDatasourceParam((DatasourceCfg) fileCfg),
                datasourceType);
        IClient client = DatasourceUtils.getDatasourceClient(datasourceType);
        logger.info("开始执行SQL:" + sql);
        List<Map<String, Object>> result = client.executeQuery(datasource, query);
        saveFile(new QueryResult(result), "json");
    }

    private DatasourceParam getDatasourceParam(DatasourceCfg cfg) {
        return cfg.getParam();
    }
}
