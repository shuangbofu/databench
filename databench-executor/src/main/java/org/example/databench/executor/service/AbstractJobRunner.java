package org.example.databench.executor.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.enums.FileType;
import org.example.databench.common.enums.JobHistoryStatus;
import org.example.databench.common.utils.JSONUtils;
import org.example.databench.executor.api.JobApi;
import org.example.databench.executor.domain.Log;
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
 * Created by shuangbofu on 2022/4/1 22:14
 */
public abstract class AbstractJobRunner implements JobApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDatasourceRunner.class);
    private static final ExecutorService pool = Executors.newFixedThreadPool(20);
    private static final Map<String, Future<?>> futureMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> logLines = new ConcurrentHashMap<>();
    private static final String cacheDir = "/tmp/databench/";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected String jobId;

    private void log(LogType logType, String msg) {
        msg = dateFormatter.format(new Date()) + " [" + logType + "] " + msg;
        logLines.computeIfAbsent(jobId, j -> new ArrayList<>()).add(msg);
    }

    private void log(LogType logType, String msg, Throwable e) {
        log(logType, msg + "\n" + ExceptionUtils.getStackTrace(e));
    }

    private String initJobId() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    @Override
    public Log fetchOffsetLog(String jobId, Long offset, Long length) {
        Log log = new Log();
        List<String> lines = Optional.ofNullable(logLines.get(jobId))
                .orElseGet(() -> readFile(jobId, "log", String.class)
                        .map(i -> Arrays.stream(i.split("\n")).collect(Collectors.toList()))
                        .orElse(Lists.newArrayList()));
        List<String> subList = lines.subList(
                Math.min(offset.intValue(), lines.size()),
                Math.min((int) (offset + length), lines.size()));
        log.setDone(isDone(jobId) && offset >= lines.size());
        log.setLines(subList);
        return log;
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
        return readFile(jobId, "status", Boolean.class)
                .filter(i -> i)
                .map(i -> JobHistoryStatus.success)
                .orElse(JobHistoryStatus.failed);
    }

    protected void saveFile(Object object, String suffix) {
        try {
            String content;
            if (object instanceof String) {
                content = object.toString();
            } else {
                content = JSONUtils.toJSONString(object);
            }
            Files.writeString(Path.of(cacheDir + jobId + "." + suffix), content);
        } catch (IOException e) {
            LOGGER.error("Write " + suffix + " error", e);
        }
    }

    protected <T> Optional<T> readFile(String jobId, String suffix, Class<T> clazz) {
        try {
            String str = Files.readString(Path.of(cacheDir + jobId + "." + suffix));
            if (clazz != null && String.class.isAssignableFrom(clazz)) {
                return Optional.ofNullable((T) str);
            }
            return Optional.ofNullable(JSONUtils.parseObject(str, clazz));
        } catch (Exception e) {
            LOGGER.error("Get " + suffix + " error", e);
        }
        return Optional.empty();
    }

    @Override
    public String executeFileJob(FileType fileType, FileContent fileContent, FileCfg fileCfg) {
        jobId = initJobId();
        JobLogger jobLogger = new JobLogger() {
            @Override
            public void warn(String msg) {
                log(LogType.WARN, msg);
            }

            @Override
            public void error(String msg) {
                log(LogType.ERROR, msg);
            }

            @Override
            public void info(String msg) {
                log(LogType.INFO, msg);
            }

            @Override
            public void error(String msg, Throwable e) {
                log(LogType.ERROR, msg, e);
            }
        };
        Future<?> future = pool.submit(() -> {
            boolean success = false;
            long start = System.currentTimeMillis();
            try {
                execute(fileType, fileContent, fileCfg, jobLogger);
                success = true;
            } catch (Exception e) {
                jobLogger.error("运行异常", e);
            } finally {
                long end = System.currentTimeMillis();
                long duration = end - start;
                jobLogger.info("执行耗时" + (duration / 1000 < 0 ? (duration + "ms") :
                        ((Math.abs((double) duration / 1000)) + "s")));
                String msg = "运行" + (success ? "成功" : "失败");
                if (success) {
                    jobLogger.info(msg);
                } else {
                    jobLogger.error(msg);
                }
                futureMap.remove(jobId);
                saveFile(String.join("\n", logLines.get(jobId)), "log");
                saveFile(success, "status");
            }
        });
        futureMap.put(jobId, future);
        return jobId;
    }

    abstract protected void execute(FileType fileType, FileContent fileContent, FileCfg fileCfg, JobLogger logger) throws Exception;

    enum LogType {
        WARN, ERROR, INFO
    }

    interface JobLogger {
        void warn(String msg);

        void error(String msg);

        void info(String msg);

        void error(String msg, Throwable e);
    }
}
