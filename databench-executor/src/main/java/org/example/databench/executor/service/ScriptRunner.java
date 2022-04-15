package org.example.databench.executor.service;

import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.enums.FileType;
import org.example.databench.executor.script.executor.CommandExecutor;
import org.example.databench.executor.script.executor.DefaultCommandExecutor;
import org.example.databench.executor.script.executor.ExecResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shuangbofu on 2022/4/1 22:48
 */
public class ScriptRunner extends AbstractJobRunner {

    private static final Map<String, CommandExecutor> executors = new ConcurrentHashMap<>();

    @Override
    protected void execute(FileType fileType, FileContent fileContent, FileCfg fileCfg, JobLogger logger) throws Exception {
        String cmd = null;
        String code = fileContent.getCode();
        if (fileType.equals(FileType.shell)) {
            cmd = code;
            logger.info("代码是\n" + cmd);
        } else if (fileType.equals(FileType.sparkSql)) {
            String sparkSqlFilePath = "/tmp/" + jobId + ".sparkSql";
            Files.writeString(Path.of(sparkSqlFilePath), code);
            cmd = "spark-sql -f " + sparkSqlFilePath;
        }
        CommandExecutor executor = new DefaultCommandExecutor()
                .logHandler((line, logLevel, logType) -> logger.info(line));
        executors.put(jobId, executor);
        cmd = "source ~/.bashrc;\n" + cmd;
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
}
