package org.example.databench.service.manager;

import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.domain.file.FileTuple;
import org.example.databench.common.domain.file.QueryCfg;
import org.example.databench.common.domain.file.datasource.DatasourceParam;
import org.example.databench.common.domain.file.datasource.JdbcParam;
import org.example.databench.common.enums.FileCategory;
import org.example.databench.common.enums.FileType;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.lib.loader.JarClassLoader;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.service.FileService;
import org.example.databench.service.FileVersionService;
import org.example.databench.service.JobHistoryService;
import org.example.databench.service.base.AbstractService;
import org.example.executor.api.ExecutableApi;
import org.example.executor.api.domain.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by shuangbofu on 2022/3/30 01:04
 * <p>
 * // TODO
 */
@Service
public class ExecutorManager extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorManager.class);
    private final Map<String, Long> fileIdCache = new ConcurrentHashMap<>();
    private final Map<String, ExecutableApi> apiCache = new ConcurrentHashMap<>();
    private final Map<String, ExecutableApi> historyApiCache = new ConcurrentHashMap<>();
    @Autowired
    private JobHistoryService jobHistoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileVersionService fileVersionService;

    public <T> T invokeByFileId(Long fileId, FileTuple fileTuple, BiFunction<ExecutableApi, ApiParam, T> function) {
        ApiParam apiParam = buildApiParam(fileId, fileTuple);
        return function.apply(getJobApi(apiParam.getExecutorType()), apiParam);
    }

    public <T> T invokeByFileId(Long fileId, BiFunction<ExecutableApi, ApiParam, T> function) {
        return invokeByFileId(fileId, null, function);
    }

    private ApiParam buildApiParam(Long fileId, FileTuple fileTuple) {
        File file = fileService.selectOneById(fileId);
        FileVersion fileVersion = fileVersionService.getByFileIdAndVersion(fileId, file.getVersion());
        if (fileTuple != null) {
            copyAToB(fileTuple, fileVersion);
        }
        if (fileVersion == null) {
            // TODO
            throw new RuntimeException("error");
        }

        ApiParam param = new ApiParam();
        param.setName(file.getName());
        param.setType(file.getFileType());
        FileContent content = (FileContent) fileVersion.getContent();
        param.setDescription(content.getDescription());
        if (file.getModuleType().equals(ModuleType.query) || file.getCategory().equals(FileCategory.database)) {
            QueryCfg cfg = (QueryCfg) fileVersion.getCfg();
            if (cfg.getDatasourceFileId() == 0) {
                throw new RuntimeException("没有选择数据源");
            }
            FileVersion datasourceFile = fileVersionService.getByFileIdAndVersion(cfg.getDatasourceFileId(),
                    cfg.getDatasourceFileVersion());
            param.setSql(content.getCode());
            DatasourceParam datasourceParam = ((DatasourceCfg) datasourceFile.getCfg()).getParam();
            if (datasourceParam instanceof JdbcParam) {
                param.setJdbcParam((JdbcParam) datasourceParam);
                // FIXME
                param.setDatasourceType(FileType.valueOf(datasourceFile.getContent().getFileType()).toDsType());
            }
            param.setExecutorType("datasourceX");
        } else if (file.getModuleType().isDevelop()) {
            param.setCode(content.getCode());
            param.setExecutorType("script");
        } else if (file.getCategory().equals(FileCategory.datasource)) {
            param.setExecutorType("datasourceX");
            param.setDatasourceType(FileType.valueOf(file.getFileType()).toDsType());
            DatasourceCfg datasourceCfg = (DatasourceCfg) fileVersion.getCfg();
            if (datasourceCfg.getParam() instanceof JdbcParam) {
                param.setJdbcParam((JdbcParam) datasourceCfg.getParam());
                param.setDatasourceType(FileType.valueOf(file.getFileType()).toDsType());
            } else {
                throw new RuntimeException("Not supported");
            }
        } else {
            throw new RuntimeException("Not supported " + file.getFileType());
        }
        return param;
    }

    private String getRunnerType(Long fileId) {
        File file = fileService.selectOneById(fileId);
        if (file.getModuleType().equals(ModuleType.query) || file.getCategory().equals(FileCategory.database) || file.getCategory().equals(FileCategory.datasource)) {
            return "datasourceX";
        } else if (file.getModuleType().isDevelop()) {
            return "script";
        }
        throw new RuntimeException("Not supported");
    }

    public <T> T invokeByHistoryId(String historyId, Function<ExecutableApi, T> function) {
        ExecutableApi executableApi = historyApiCache.computeIfAbsent(historyId, i -> {
            Long fieldId = fileIdCache.computeIfAbsent(historyId,
                    j -> jobHistoryService.selectByJobId(historyId).getFileId());
            return getJobApi(getRunnerType(fieldId));
        });
        return function.apply(executableApi);
    }

    private ExecutableApi getJobApi(String runnerType) {
        return apiCache.computeIfAbsent(runnerType, i -> createJobApi(runnerType));
    }

    private ExecutableApi createJobApi(String runnerType) {
        JarClassLoader<ExecutableApi> classLoader = new JarClassLoader<>("/tmp/lib/", ExecutableApi.class);
        Optional<ExecutableApi> jobApi;
        try {
            jobApi = classLoader.loadFirst(runnerType);
            return jobApi.orElseThrow(() -> new RuntimeException(runnerType + " not supported"));
        } catch (Exception e) {
            throw new RuntimeException("Load api error", e);
        }
    }
}
