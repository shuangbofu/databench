package org.example.databench.executor.script;

import org.example.databench.executor.script.executor.CommandExecutor;
import org.example.databench.executor.script.executor.DefaultCommandExecutor;
import org.example.databench.executor.script.executor.ExecResult;
import org.example.executor.api.api.JobApi;
import org.example.executor.api.domain.ApiParam;
import org.example.executor.api.domain.query.QueryResult;
import org.example.executor.base.service.AbstractLocalJobRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shuangbofu on 2022/4/1 22:48
 */
public class ScriptRunner extends AbstractLocalJobRunner implements JobApi {

    private static final Map<String, CommandExecutor> executors = new ConcurrentHashMap<>();

    @Override
    protected void execute(ApiParam param, JobLogger logger) throws Exception {
        String cmd = null;
        String code = param.getCode();
        if (param.getType().equals("shell")) {
            cmd = code;
            logger.info("代码是\n" + cmd);
        } else if (param.getType().equals("sparkSql")) {
            String sparkSqlFilePath = "/tmp/" + jobId + ".sparkSql";
            Files.writeString(Path.of(sparkSqlFilePath), code);
            cmd = "spark-sql -f " + sparkSqlFilePath;
        }
        CommandExecutor executor = new DefaultCommandExecutor()
                .logHandler((line, logLevel, logType) -> logger.info(line));
        executors.put(jobId, executor);
        if (new File("~/.bashrc").exists()) {
            cmd = "source ~/.bashrc;\n" + cmd;
        }
        ExecResult execResult = executor.execScript(cmd);
        if (!execResult.isSuccess()) {
            throw new RuntimeException(execResult.getException());
        }
    }

    @Override
    public boolean cancel(String jobId) {
        if (executors.containsKey(jobId)) {
            return executors.get(jobId).stop();
        }
        return false;
    }

    @Override
    public boolean checkConnection(ApiParam param) {
        return false;
    }

    @Override
    public QueryResult fetchResult(String jobId) {
        return new QueryResult();
    }
}
