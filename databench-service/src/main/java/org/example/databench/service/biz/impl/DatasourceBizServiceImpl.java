package org.example.databench.service.biz.impl;

import org.example.databench.service.biz.DatasourceBizService;
import org.example.databench.service.manager.ExecutorManager;
import org.example.executor.api.ExecutableApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2022/3/30 00:31
 */
@Service
public class DatasourceBizServiceImpl implements DatasourceBizService {

    @Autowired
    private ExecutorManager executorManager;

    @Override
    public boolean checkConnection(Long fileId) {
        return executorManager.invokeByFileId(fileId, ExecutableApi::checkConnection);
    }
}
