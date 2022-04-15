package org.example.databench.web.controller;

import org.example.databench.common.vo.PageVO;
import org.example.databench.service.JobHistoryService;
import org.example.databench.service.biz.ConsoleBizService;
import org.example.databench.service.domain.param.JobHistoryFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.JobHistoryVO;
import org.example.databench.web.annotations.ResultController;
import org.example.databench.web.config.DaoContext;
import org.example.executor.api.domain.Log;
import org.example.executor.api.domain.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by shuangbofu on 2022/3/31 20:13
 */
@ResultController("/api/console")
public class ConsoleController extends BaseController {
    @Autowired
    private ConsoleBizService consoleBizService;
    @Autowired
    private JobHistoryService jobHistoryService;

    public ConsoleController(@Autowired DaoContext daoContext) {
        super(daoContext);
    }

    @PostMapping("/checkDone")
    public boolean checkDone(String jobId) {
        return consoleBizService.checkJobDone(jobId);
    }

    @GetMapping("/queryResult")
    public QueryResult fetchQueryResult(String jobId) {
        return consoleBizService.fetchQueryResult(jobId);
    }

    @GetMapping("/log")
    public Log fetchOffsetLog(String jobId, Long offset, Long length) {
        return consoleBizService.fetchOffsetLog(jobId, offset, length);
    }

    @PostMapping("/jobHistory/page")
    PageVO<JobHistoryVO> getPage(@RequestBody PageFilterParam<JobHistoryFilter> param) {
        daoContext(param.getFilter().getWorkspaceId(), param.getFilter().getBizId());
        return consoleBizService.getJobHistories(param);
    }

    @GetMapping("/jobHistory")
    public JobHistoryVO getJobHistory(@RequestParam("jobId") String jobId) {
        return consoleBizService.getHistory(jobId);
    }

    @PostMapping("/cancel/{jobId}")
    public Boolean cancelJob(@PathVariable("jobId") String jobId) {
        return consoleBizService.cancelJob(jobId);
    }
}
