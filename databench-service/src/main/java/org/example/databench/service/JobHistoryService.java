package org.example.databench.service;

import org.example.databench.common.vo.PageVO;
import org.example.databench.persistence.entity.JobHistory;
import org.example.databench.service.base.BaseService;
import org.example.databench.service.domain.param.JobHistoryFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.JobHistoryVO;

/**
 * Created by shuangbofu on 2022/3/31 16:07
 */
public interface JobHistoryService extends BaseService<JobHistory> {
    PageVO<JobHistoryVO> getPage(PageFilterParam<JobHistoryFilter> param);

    JobHistory selectByJobId(String jobId);
}
