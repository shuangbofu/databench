package org.example.databench.executor.script;

import org.example.databench.executor.script.executor.CommandExecutor;
import org.example.databench.executor.script.executor.DefaultCommandExecutor;
import org.example.databench.executor.script.executor.ExecResult;
import org.example.executor.api.ExecutableApi;
import org.example.executor.api.domain.ApiParam;
import org.example.executor.api.domain.query.QueryResult;
import org.example.executor.base.service.AbstractLocalExecutor;
import org.example.executor.base.service.log.ExecuteLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shuangbofu on 2022/4/1 22:48
 */
public class ScriptExecutor extends AbstractLocalExecutor implements ExecutableApi {

    private static final Map<String, CommandExecutor> executors = new ConcurrentHashMap<>();

    @Override
    protected void execute(ApiParam param, String jobId, ExecuteLogger LOG) throws Exception {
        String type = param.getType();
        String code = param.getCode();
        String command = buildCommand(LOG, jobId, type, code);
        CommandExecutor executor = new DefaultCommandExecutor().commandUser(param.getTenant())
                .logHandler((line, logLevel, logType) -> LOG.log(line));
        executors.put(jobId, executor);
        ExecResult execResult = executor.execScript(command);
        if (!execResult.isSuccess()) {
            throw new RuntimeException(execResult.getException());
        }
    }

    private String buildCommand(ExecuteLogger LOG, String jobId, String type, String code) throws IOException {
        LOG.info("代码是：\n" + code);
        String command;
        if (type.equals("shell")) {
            command = code;
        } else if (type.equals("sparkSql")) {
            String sparkSqlFilePath = "/tmp/" + jobId + ".sparkSql";
            Files.writeString(Path.of(sparkSqlFilePath), code);
            command = "spark-sql -f " + sparkSqlFilePath;
        } else {
            throw new RuntimeException("Not supported " + type);
        }
        if (Files.exists(Path.of("~/.bashrc"))) {
            command = "source ~/.bashrc;\n" + command;
        }
        return command;
    }

    @Override
    public boolean cancel(String jobId) {
        if (executors.containsKey(jobId)) {
            return executors.get(jobId).stop();
        }
        super.cancel(jobId);
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
