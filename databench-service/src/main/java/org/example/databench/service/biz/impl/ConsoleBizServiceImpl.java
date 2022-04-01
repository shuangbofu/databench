package org.example.databench.service.biz.impl;

import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.common.vo.PageVO;
import org.example.databench.executor.domain.Log;
import org.example.databench.persistence.entity.JobHistory;
import org.example.databench.service.JobHistoryService;
import org.example.databench.service.base.AbstractService;
import org.example.databench.service.biz.ConsoleBizService;
import org.example.databench.service.domain.param.JobHistoryFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.JobHistoryVO;
import org.example.databench.service.manager.ExecutorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Created by shuangbofu on 2022/3/31 16:58
 */
@Service
public class ConsoleBizServiceImpl extends AbstractService implements ConsoleBizService {

    @Autowired
    private ExecutorManager executorManager;
    private final Consumer<JobHistoryVO> consumer = (i) -> {
        if (!i.getDone()) {
            i.setDone(checkJobDone(i.getJobId()));
        }
        if (i.getDone()) {
            i.setStatus(executorManager.getDatasourceApi().getStatus(i.getJobId()));
        }
    };
    @Autowired
    private JobHistoryService jobHistoryService;

    @Override
    public PageVO<JobHistoryVO> getJobHistories(PageFilterParam<JobHistoryFilter> param) {
        PageVO<JobHistoryVO> page = jobHistoryService.getPage(param);
        page.getData().forEach(consumer);
        return page;
    }

    @Override
    public JobHistoryVO getHistory(String jobId) {
        JobHistory history = jobHistoryService.selectByJobId(jobId);
        JobHistoryVO jobHistoryVO = aToB(history, JobHistoryVO.class);
        consumer.accept(jobHistoryVO);
        return jobHistoryVO;
    }

    @Override
    public boolean checkJobDone(String jobId) {
        return executorManager.getJobApi().isDone(jobId);
    }

    @Override
    public Log fetchOffsetLog(String jobId, Long offset, Long length) {
        return executorManager.getJobApi().fetchOffsetLog(jobId, offset, length);
    }

    @Override
    public QueryResult fetchQueryResult(String jobId) {
        return executorManager.getDatasourceApi().fetchResult(jobId);
    }

    @Override
    public Boolean cancelJob(String jobId) {
        return executorManager.getJobApi().cancel(jobId);
    }
}
