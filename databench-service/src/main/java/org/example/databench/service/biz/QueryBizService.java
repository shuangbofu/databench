package org.example.databench.service.biz;

import org.example.databench.common.domain.query.QueryResult;

/**
 * Created by shuangbofu on 2022/3/29 00:14
 */
public interface QueryBizService {
    QueryResult runQueryFile(Long id);
}
