package org.example.executor.base.service;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.databench.common.enums.JobHistoryStatus;
import org.example.databench.lib.utils.FileUtils;
import org.example.executor.api.ExecutableApi;
import org.example.executor.api.domain.ApiParam;
import org.example.executor.api.domain.Log;
import org.example.executor.base.service.db.Db;
import org.example.executor.base.service.db.LocalFileDb;
import org.example.executor.base.service.log.ExecuteLogger;
import org.example.executor.base.service.log.MemoLogStore;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2022/4/1 22:14
 */
public abstract class AbstractLocalExecutor implements ExecutableApi {
    private static final String STATUS_KEY = "status";
    private static final String LOG_KEY = "log";
    private static final Map<Class<? extends AbstractLocalExecutor>, String> bannerCache = new ConcurrentHashMap<>();
    private final ExecutorService pool = Executors.newFixedThreadPool(20, new ThreadFactoryBuilder()
            .setNameFormat("executor-pool-%d").build());
    private final Map<String, Future<?>> futures = new ConcurrentHashMap<>();
    private final Map<String, Db> dbs = new ConcurrentHashMap<>();
    private final Map<String, MemoLogStore> logStores = new ConcurrentHashMap<>();

    private String initJobId() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    @Override
    public Log fetchOffsetLog(String jobId, Long offset, Long length) {
        Log log = new Log();
        List<String> strings = Optional.ofNullable(logStores.get(jobId)).map(MemoLogStore::getLogLines).orElse(null);
        List<String> lines = Optional.ofNullable(strings)
                .orElseGet(() -> getDb(jobId).get(LOG_KEY, String.class)
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
        return Optional.ofNullable(futures.get(jobId)).map(Future::isDone).orElse(true);
    }

    @Override
    public boolean cancel(String jobId) {
        if (futures.containsKey(jobId)) {
            futures.get(jobId).cancel(true);
        }
        return true;
    }

    protected Db getDb(String jobId) {
        return dbs.computeIfAbsent(jobId, i -> new LocalFileDb(jobId));
    }

    @Override
    public JobHistoryStatus getStatus(String jobId) {
        return getDb(jobId).get(STATUS_KEY, Boolean.class)
                .filter(i -> i)
                .map(i -> JobHistoryStatus.success)
                .orElse(JobHistoryStatus.failed);
    }

    @Override
    public String executeFileJob(ApiParam param) {
        String jobId = initJobId();
        LocalFileDb db = new LocalFileDb(jobId);
        MemoLogStore LOG = new MemoLogStore();
        logStores.put(jobId, LOG);
        futures.put(jobId, pool.submit(() -> {
            boolean success = false;
            long start = System.currentTimeMillis();
            try {
                printBanner(LOG);
                LOG.info("开始执行");
                execute(param, jobId, LOG);
                success = true;
            } catch (Exception e) {
                LOG.error("运行异常", e);
            } finally {
                long end = System.currentTimeMillis();
                long duration = end - start;
                LOG.info("执行耗时" + (duration / 1000 < 0 ? (duration + "ms") :
                        ((Math.abs((double) duration / 1000)) + "s")));
                String msg = "运行" + (success ? "成功" : "失败");
                if (success) {
                    LOG.info(msg);
                } else {
                    LOG.error(msg);
                }
                futures.remove(jobId);
                db.set(LOG_KEY, String.join("\n", LOG.getLogLines()));
                db.set(STATUS_KEY, success);
            }
        }));
        return jobId;
    }

    private void printBanner(ExecuteLogger logger) {
        try {
            String banner = bannerCache.computeIfAbsent(getClass(), i -> {
                try {
                    return FileUtils.loadResourceFile("banner.txt");
                } catch (IOException ignored) {
                    throw new RuntimeException("error");
                }
            });
            logger.info("\n" + banner + "\n");
        } catch (Exception ignored) {
        }
    }

    abstract protected void execute(ApiParam param, String jobId, ExecuteLogger LOG) throws Exception;
}
