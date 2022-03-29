package org.example.databench.executor.utils;

import com.dtstack.dtcenter.loader.cache.pool.config.PoolConfig;
import com.dtstack.dtcenter.loader.client.ClientCache;
import com.dtstack.dtcenter.loader.client.IClient;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import com.dtstack.dtcenter.loader.dto.source.RdbmsSourceDTO;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import org.example.databench.common.domain.file.datasource.DatasourceParam;
import org.example.databench.common.domain.file.datasource.JdbcParam;
import org.example.databench.common.enums.DatasourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shuangbofu on 2022/3/30 00:49
 */
public class DatasourceUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasourceUtils.class);

    static {
        ClientCache.setUserDir("/tmp/datasourceLibs");
    }

    public static ISourceDTO getDatasource(DatasourceParam param, DatasourceType datasourceType) {
        DataSourceType type = toDsType(datasourceType);
        if (datasourceType.supportJdbc()) {
            JdbcParam jdbcParam = (JdbcParam) param;
            return RdbmsSourceDTO.builder()
                    .sourceType(type.getVal())
                    .url(jdbcParam.getJdbcUrl())
                    .username(jdbcParam.getUsername())
                    .password(jdbcParam.getPassword())
                    .poolConfig(PoolConfig.builder().build())
                    .build();
        }
        throw new RuntimeException("Not supported " + datasourceType);
    }

    private static DataSourceType toDsType(DatasourceType datasourceType) {
        return DataSourceType.valueOf(datasourceType.name());
    }

    public static IClient getDatasourceClient(DatasourceType datasourceType) {
        try {
            return ClientCache.getClient(toDsType(datasourceType).getVal());
        } catch (Exception e) {
            throw new RuntimeException("Get datasource client error", e);
        }
    }

    public static boolean checkConnection(DatasourceParam param, DatasourceType datasourceType) {
        try {
            return getDatasourceClient(datasourceType)
                    .testCon(getDatasource(param, datasourceType));
        } catch (Exception e) {
            LOGGER.error("Check connection error", e);
            return false;
        }
    }
}
