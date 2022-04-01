package org.example.databench.service.impl;

import org.example.databench.common.vo.PageVO;
import org.example.databench.persistence.dao.JobHistoryDao;
import org.example.databench.persistence.entity.JobHistory;
import org.example.databench.service.JobHistoryService;
import org.example.databench.service.base.CommonService;
import org.example.databench.service.domain.param.JobHistoryFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.JobHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2022/3/31 16:07
 */
@Service
public class JobHistoryServiceImpl extends CommonService<JobHistory, JobHistoryDao> implements JobHistoryService {

    public JobHistoryServiceImpl(@Autowired JobHistoryDao dao) {
        super(dao);
    }

    @Override
    public PageVO<JobHistoryVO> getPage(PageFilterParam<JobHistoryFilter> param) {
        JobHistoryFilter filter = param.getFilter();
        return toVOPage(param.getPageNum(), param.getPageSize(), q -> {
            q.lambda().eq(JobHistory::getFileId, filter.getFileId())
                    .orderByDesc(JobHistory::getCreateTime);
        }, JobHistoryVO.class);
    }

    @Override
    public JobHistory selectByJobId(String jobId) {
        return selectOneBy(q -> q.lambda().eq(JobHistory::getJobId, jobId));
    }
}
