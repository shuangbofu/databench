package org.example.executor.api;

import org.example.databench.common.enums.JobHistoryStatus;
import org.example.executor.api.domain.ApiParam;
import org.example.executor.api.domain.Log;
import org.example.executor.api.domain.query.QueryResult;

/**
 * Created by shuangbofu on 2022/3/31 16:22
 */
public interface ExecutableApi {
    Log fetchOffsetLog(String jobId, Long offset, Long length);

    boolean isDone(String jobId);

    boolean cancel(String jobId);

    JobHistoryStatus getStatus(String jobId);

    String executeFileJob(ApiParam param);

    boolean checkConnection(ApiParam param);

    QueryResult fetchResult(String jobId);
}
