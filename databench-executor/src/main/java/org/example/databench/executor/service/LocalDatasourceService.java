package org.example.databench.executor.service;

import com.dtstack.dtcenter.loader.client.IClient;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.databench.common.domain.file.DatasourceCfg;
import org.example.databench.common.domain.file.datasource.DatasourceParam;
import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.common.enums.DatasourceType;
import org.example.databench.common.enums.JobHistoryStatus;
import org.example.databench.common.utils.JSONUtils;
import org.example.databench.executor.api.DatasourceApi;
import org.example.databench.executor.domain.Log;
import org.example.databench.executor.utils.DatasourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2022/3/30 01:07
 */
public class LocalDatasourceService implements DatasourceApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDatasourceService.class);
    private static final ExecutorService pool = Executors.newFixedThreadPool(20);
    private static final Map<String, Future<?>> futureMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> logLines = new ConcurrentHashMap<>();
    private static final String cacheDir = "/tmp/databench/";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String initJobId() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    @Override
    public boolean checkConnection(DatasourceCfg datasourceCfg, DatasourceType datasourceType) {
        return DatasourceUtils.checkConnection(getDatasourceParam(datasourceCfg), datasourceType);
    }

    @Override
    public String queryResult(DatasourceCfg datasourceCfg, DatasourceType datasourceType, String code) {
        SqlQueryDTO query = SqlQueryDTO.builder().sql(code).build();
        ISourceDTO datasource = DatasourceUtils.getDatasource(getDatasourceParam(datasourceCfg), datasourceType);
        IClient client = DatasourceUtils.getDatasourceClient(datasourceType);
        String jobId = initJobId();
        Future<?> future = pool.submit(() -> {
            log(true, jobId, "执行的SQL是\n" + query.getSql());
            log(true, jobId, "开始运行");
            boolean success = false;
            try {
                QueryResult queryResult = new QueryResult(client.executeQuery(datasource, query));
                success = true;
                try {
                    Files.writeString(getCacheFilePath(jobId, "json"),
                            JSONUtils.toJSONString(queryResult, true));
                } catch (IOException e) {
                    LOGGER.error("Write result error", e);
                }
            } catch (Exception e) {
                log(false, jobId, ExceptionUtils.getStackTrace(e));
            } finally {
                log(true, jobId, "运行" + (success ? "成功" : "失败"));
                futureMap.remove(jobId);
                try {
                    Files.writeString(getCacheFilePath(jobId, "log"),
                            String.join("\n", logLines.get(jobId)));
                } catch (IOException e) {
                    LOGGER.error("Write log error", e);
                }

                try {
                    Files.writeString(getCacheFilePath(jobId, "status"), success + "");
                } catch (IOException e) {
                    LOGGER.error("Write status error", e);
                }
            }
        });
        futureMap.put(jobId, future);
        return jobId;
    }

    private void log(boolean info, String jobId, String msg) {
        msg = (info ? "[INFO] " : "[ERROR]") + dateFormatter.format(new Date()) + " " + msg;
        logLines.computeIfAbsent(jobId, j -> new ArrayList<>()).add(msg);
    }

    private Path getCacheFilePath(String jobId, String suffix) {
        return Path.of(cacheDir + jobId + "." + suffix);
    }

    @Override
    public QueryResult fetchResult(String jobId) {
        try {
            return JSONUtils.parseObject(Files.readString(getCacheFilePath(jobId, "json")),
                    QueryResult.class);
        } catch (IOException e) {
            LOGGER.warn("Fetch result error", e);
            return new QueryResult();
        }
    }

    @Override
    public Log fetchOffsetLog(String jobId, Long offset, Long length) {
        Log log = new Log();
        List<String> lines = Optional.ofNullable(logLines.get(jobId)).orElseGet(() -> {
            try {
                return Arrays.stream(Files.readString(getCacheFilePath(jobId, "log")).split("\n"))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                LOGGER.warn("Get log error", e);
                return Lists.newArrayList();
            }
        });
        List<String> subList = lines.subList(offset.intValue(), Math.min((int) (offset + length), lines.size()));
        log.setDone(isDone(jobId) && offset >= lines.size());
        log.setLines(subList);
        return log;
    }

    @Override
    public Log fetchLog(String jobId) {
        return null;
    }

    @Override
    public boolean isDone(String jobId) {
        return Optional.ofNullable(futureMap.get(jobId)).map(Future::isDone).orElse(true);
    }

    @Override
    public boolean cancel(String jobId) {
        if (futureMap.containsKey(jobId)) {
            futureMap.get(jobId).cancel(true);
        }
        return true;
    }

    @Override
    public JobHistoryStatus getStatus(String jobId) {
        try {
            return Boolean.parseBoolean(Files.readString(getCacheFilePath(jobId, "status")))
                    ? JobHistoryStatus.success : JobHistoryStatus.failed;
        } catch (IOException e) {
            LOGGER.error("Get status error");
        }
        return JobHistoryStatus.failed;
    }

    private DatasourceParam getDatasourceParam(DatasourceCfg cfg) {
        return cfg.getParam();
    }
}
