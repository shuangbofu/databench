package org.example.databench.service.biz;

import org.example.databench.common.vo.PageVO;
import org.example.databench.service.domain.param.JobHistoryFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.JobHistoryVO;
import org.example.executor.api.domain.Log;
import org.example.executor.api.domain.query.QueryResult;

/**
 * Created by shuangbofu on 2022/3/31 16:58
 */
public interface ConsoleBizService {
    PageVO<JobHistoryVO> getJobHistories(PageFilterParam<JobHistoryFilter> param);

    JobHistoryVO getHistory(String jobId);

    boolean checkJobDone(String jobId);

    Log fetchOffsetLog(String jobId, Long offset, Long length);

    QueryResult fetchQueryResult(String jobId);

    Boolean cancelJob(String jobId);
}
