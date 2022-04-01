package org.example.databench.service.manager;

import org.example.databench.executor.api.DatasourceApi;
import org.example.databench.executor.api.JobApi;
import org.example.databench.executor.service.LocalDatasourceRunner;
import org.example.databench.executor.service.ScriptRunner;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2022/3/30 01:04
 * <p>
 * // TODO
 */
@Service
public class ExecutorManager {

    public DatasourceApi getDatasourceApi() {
        return new LocalDatasourceRunner();
    }

    public JobApi getJobApi() {
        return new ScriptRunner();
    }
}
