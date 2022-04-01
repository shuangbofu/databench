package org.example.databench.executor.script.executor;

import org.example.databench.executor.script.log.CommandLogHandler;
import org.apache.commons.exec.CommandLine;

public interface CommandExecutor {

    ExecResult exec(CommandLine commandLine);

    ExecResult execScript(String script, String... scriptSavePath);

    ExecResult exec(String commandLine);

    boolean stop();

    CommandExecutor logHandler(CommandLogHandler commandLogHandler);

    CommandExecutor logHandler(CommandLogHandler commandLogHandler, int level);

    CommandExecutor timeout(long timeout);

    CommandLogHandler getLogHandler();
}
