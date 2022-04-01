package org.example.databench.executor.script.utils;

import org.example.databench.executor.script.executor.*;
import org.example.databench.executor.script.log.SimpleCommandLogCollector;
import org.apache.commons.exec.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class ShellUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellUtils.class);

    public static String execCommand(CommandLine commandLine) throws ShellException {
        return exec(i -> i.exec(commandLine));
    }

    public static String execCommand(String cmd) throws ShellException {
        return exec(i -> i.exec(cmd));
    }

    public static String execScript(String script) throws ShellException {
        return exec(i -> i.execScript(script));
    }

    public static String scp(SecureAuth secureAuth, String source, String target, boolean to, String... options) throws ShellException {
        return exec(secureAuth, i -> ((RemoteCommandExecutor) i).scp(source, target, to, options));
    }

    public static String execRemoteCommand(SecureAuth secureAuth, CommandLine commandLine) throws ShellException {
        return exec(secureAuth, i -> i.exec(commandLine));
    }

    public static String execRemoteCommand(SecureAuth secureAuth, String cmd) throws ShellException {
        return exec(secureAuth, i -> i.exec(cmd));
    }

    public static String execRemoteScript(SecureAuth secureAuth, String script) throws ShellException {
        return exec(secureAuth, i -> i.execScript(script));
    }

    private static String exec(SecureAuth secureAuth, Function<AbstractCommandExecutor, ExecResult> function) throws ShellException {
        var logCollector = new SimpleCommandLogCollector();
        AbstractCommandExecutor commandExecutor;
        if (secureAuth != null) {
            commandExecutor = new RemoteCommandExecutor(secureAuth);
        } else {
            commandExecutor = new DefaultCommandExecutor();
        }
        commandExecutor.logHandler(logCollector);
        var result = function.apply(commandExecutor);
        return parseResult(result, logCollector);
    }

    private static String exec(Function<AbstractCommandExecutor, ExecResult> function) throws ShellException {
        return exec(null, function);
    }

    private static String parseResult(ExecResult execResult, SimpleCommandLogCollector collector) throws ShellException {
        if (execResult != null && execResult.isSuccess()) {
            return collector.getOut();
        } else {
            var errorLog = collector.getErr();
            if (execResult != null && (errorLog == null || errorLog.isEmpty())) {
                var exception = execResult.getException();
                if (exception != null) {
                    throw new ShellException(exception);
                }
            }
            throw new ShellException("Execute command error, " + errorLog);
        }
    }
}
