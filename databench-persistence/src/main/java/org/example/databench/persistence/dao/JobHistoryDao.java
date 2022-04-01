package org.example.databench.persistence.dao;

import io.github.shuangbofu.helper.dao.BaseDao;
import org.example.databench.persistence.dao.mapper.JobHistoryMapper;
import org.example.databench.persistence.entity.JobHistory;

/**
 * Created by shuangbofu on 2022/3/31 16:06
 */
public interface JobHistoryDao extends BaseDao<JobHistory, JobHistoryMapper> {
}
