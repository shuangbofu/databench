package org.example.databench.service.manager;

import org.example.databench.executor.api.DatasourceApi;
import org.example.databench.executor.service.LocalDatasourceService;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2022/3/30 01:04
 * <p>
 * // TODO
 */
@Service
public class ExecutorManager {

    public DatasourceApi getDatasourceApi() {
        return new LocalDatasourceService();
    }
}
