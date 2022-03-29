package org.example.databench.web.controller.resource;

import org.example.databench.service.biz.DatasourceBizService;
import org.example.databench.web.annotations.ResultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by shuangbofu on 2022/3/29 23:55
 */
@ResultController("api/resource/datasource")
public class DatasourceController {

    @Autowired
    private DatasourceBizService datasourceBizService;

    @PostMapping("checkConnection")
    public boolean checkConnection(Long fileId) {
        return datasourceBizService.checkConnection(fileId);
    }
}
