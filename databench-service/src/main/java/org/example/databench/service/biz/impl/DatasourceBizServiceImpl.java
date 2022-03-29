package org.example.databench.service.biz.impl;

import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.persistence.entity.File;
import org.example.databench.service.FileService;
import org.example.databench.service.FileVersionService;
import org.example.databench.service.biz.DatasourceBizService;
import org.example.databench.service.manager.ExecutorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2022/3/30 00:31
 */
@Service
public class DatasourceBizServiceImpl implements DatasourceBizService {

    @Autowired
    private ExecutorManager executorManager;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileVersionService fileVersionService;

    @Override
    public boolean checkConnection(Long fileId) {
        File file = fileService.selectOneById(fileId);
        FileCfg cfg = fileVersionService.getCfgByFileIdAndVersion(fileId, file.getVersion());
        DatasourceCfg datasourceCfg = (DatasourceCfg) cfg;
        return executorManager.getDatasourceApi()
                .checkConnection(datasourceCfg, file.getFileType().toDsType());
    }
}
