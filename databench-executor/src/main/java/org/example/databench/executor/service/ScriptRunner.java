package org.example.databench.executor.service;

import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.enums.FileType;
import org.example.databench.executor.script.executor.CommandExecutor;
import org.example.databench.executor.script.executor.DefaultCommandExecutor;
import org.example.databench.executor.script.executor.ExecResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shuangbofu on 2022/4/1 22:48
 */
public class ScriptRunner extends AbstractJobRunner {

    private static final Map<String, CommandExecutor> executors = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        CommandExecutor executor = new DefaultCommandExecutor()
                .logHandler((line, logLevel, logType) -> {
                    System.out.println(line);
                });

        new Thread(() -> {
            executor.execScript("#!/bin/bash\n" +
                    "for i in $(seq 300)\n" +
                    "do\n" +
                    "    sleep 1\n" +
                    "    echo $i\n" +
                    "done");
        }).start();

        Thread.sleep(2000);
        executor.stop();

    }

    @Override
    protected void execute(FileType fileType, FileContent fileContent, FileCfg fileCfg, JobLogger logger) throws Exception {
        if (fileType.equals(FileType.shell)) {
            String code = fileContent.getCode();
            CommandExecutor executor = new DefaultCommandExecutor()
                    .logHandler((line, logLevel, logType) -> {
                        logger.info(line);
                    });
            logger.info("代码是" + code);
            executors.put(jobId, executor);
            ExecResult execResult = executor.execScript(code);
            if (!execResult.isSuccess()) {
                throw new RuntimeException(execResult.getException());
            }
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
