package org.example.databench.service.biz.impl;

import com.google.common.collect.Lists;
import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.domain.file.QueryCfg;
import org.example.databench.common.domain.file.datasource.DatasourceParam;
import org.example.databench.common.domain.file.datasource.JdbcParam;
import org.example.databench.common.domain.query.result.MetaColumn;
import org.example.databench.common.domain.query.result.RdsOpResult;
import org.example.databench.common.domain.query.result.SqlResult;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.common.utils.JdbcConnectParam;
import org.example.databench.common.utils.JdbcConnection;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.service.FileService;
import org.example.databench.service.FileVersionService;
import org.example.databench.service.biz.FileBizService;
import org.example.databench.service.biz.QueryBizService;
import org.example.databench.service.domain.vo.FileDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

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


    @Override
    public Object runQueryFile(Long id) {
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
        DatasourceParam param = datasourceCfg.getParam();
        if (datasourceFile.getContent().getFileType().isJdbcResourceFile()) {
            JdbcParam jdbcParam = (JdbcParam) param;
            try {
                JdbcConnection jdbcConnection = new JdbcConnection(new JdbcConnectParam(jdbcParam.getJdbcUrl(),
                        jdbcParam.getUsername(), jdbcParam.getPassword()));
                jdbcConnection.isValid(1000);
                SqlResult sqlResult = jdbcConnection.executeQuery(queryCode);
                return new RdsOpResult(sqlResult.getData(),
                        Optional.ofNullable(sqlResult.getResultMeta()).map(i -> i.getMetaColumns().stream()
                                .map(MetaColumn::getName).collect(Collectors.toList())).orElse(Lists.newArrayList()));
            } catch (Exception e) {
                throw new RuntimeException("执行异常" + e.getMessage());
            }
        }
        return null;
    }
}
