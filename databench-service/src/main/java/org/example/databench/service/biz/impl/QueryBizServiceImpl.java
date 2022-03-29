package org.example.databench.service.biz.impl;

import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.domain.file.QueryCfg;
import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.service.FileService;
import org.example.databench.service.FileVersionService;
import org.example.databench.service.biz.FileBizService;
import org.example.databench.service.biz.QueryBizService;
import org.example.databench.service.domain.vo.FileDetailVO;
import org.example.databench.service.manager.ExecutorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2022/3/29 00:15
 */
@Service
public class QueryBizServiceImpl implements QueryBizService {

    @Autowired
    private FileBizService fileBizService;

    @Autowired
    private FileVersionService fileVersionService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ExecutorManager executorManager;

    @Override
    public QueryResult runQueryFile(Long id) {
        File file = fileService.selectOneById(id);
        if (!file.getBelong().equals(ModuleType.query)) {
            throw new RuntimeException("不是运行文件");
        }
        FileDetailVO fileDetail = fileBizService.getFileDetail(id);
        FileContent content = (FileContent) fileDetail.getContent();
        String queryCode = content.getCode();
        QueryCfg cfg = (QueryCfg) fileDetail.getCfg();
        if (cfg.getDatasourceFileId() == 0) {
            throw new RuntimeException("没有选择数据源");
        }
        FileVersion datasourceFile = fileVersionService.getByFileIdAndVersion(cfg.getDatasourceFileId(),
                cfg.getDatasourceFileVersion());
        DatasourceCfg datasourceCfg = (DatasourceCfg) datasourceFile.getCfg();
        return executorManager.getDatasourceApi().queryResult(datasourceCfg,
                datasourceFile.getContent().getFileType().toDsType(), queryCode);
    }
}
