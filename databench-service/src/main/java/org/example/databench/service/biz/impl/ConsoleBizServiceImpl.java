package org.example.databench.service.biz.impl;

import org.example.databench.common.enums.JobHistoryStatus;
import org.example.databench.common.vo.PageVO;
import org.example.databench.persistence.entity.JobHistory;
import org.example.databench.service.FileService;
import org.example.databench.service.JobHistoryService;
import org.example.databench.service.base.AbstractService;
import org.example.databench.service.biz.ConsoleBizService;
import org.example.databench.service.domain.param.JobHistoryFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.JobHistoryVO;
import org.example.databench.service.manager.ExecutorManager;
import org.example.executor.api.domain.Log;
import org.example.executor.api.domain.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Created by shuangbofu on 2022/3/31 16:58
 */
@Service
public class ConsoleBizServiceImpl extends AbstractService implements ConsoleBizService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleBizService.class);
    @Autowired
    private ExecutorManager executorManager;

    @Autowired
    private FileService fileService;

    private final Consumer<JobHistoryVO> consumer = (i) -> {
        if (!i.getDone()) {
            boolean done = checkJobDone(i.getJobId());
            LOGGER.info("{} done {}", i.getJobId(), done);
            i.setDone(done);
        }
        if (i.getDone()) {
            JobHistoryStatus jobHistoryStatus = executorManager.invokeByHistoryId(i.getJobId(), j -> j.getStatus(i.getJobId()));
            LOGGER.info("{} status {}", i.getJobId(), jobHistoryStatus);
            i.setStatus(jobHistoryStatus);
        }
        i.setName(fileService.getName(i.getFileId()));
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
        return executorManager.invokeByHistoryId(jobId, i -> i.isDone(jobId));
    }

    @Override
    public Log fetchOffsetLog(String jobId, Long offset, Long length) {
        return executorManager.invokeByHistoryId(jobId, i -> i.fetchOffsetLog(jobId, offset, length));
    }

    @Override
    public QueryResult fetchQueryResult(String jobId) {
        return executorManager.invokeByHistoryId(jobId, i -> i.fetchResult(jobId));
    }

    @Override
    public Boolean cancelJob(String jobId) {
        return executorManager.invokeByHistoryId(jobId, i -> i.cancel(jobId));
    }
}
