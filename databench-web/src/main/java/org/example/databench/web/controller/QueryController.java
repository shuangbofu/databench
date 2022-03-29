package org.example.databench.web.controller;

import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.service.biz.QueryBizService;
import org.example.databench.web.annotations.ResultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by shuangbofu on 2022/3/28 14:05
 */
@ResultController("/api/query")
public class QueryController {

    @Autowired
    private QueryBizService queryBizService;

    @PostMapping("run")
    public QueryResult runQueryFile(Long fileId) {
        return queryBizService.runQueryFile(fileId);
    }
}
