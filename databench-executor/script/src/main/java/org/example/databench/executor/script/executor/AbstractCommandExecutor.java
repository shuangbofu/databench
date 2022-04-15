package org.example.databench.executor.script.executor;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.example.databench.executor.script.constant.Constants;
import org.example.databench.executor.script.log.CommandLogHandler;
import org.example.databench.executor.script.log.LogType;
import org.example.databench.lib.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Abstract command executor
 * 1.create script file and execute file
 * 2.wrapped by user
 * 3.execute command
 * 4.handler execute command log and close
 */
public abstract class AbstractCommandExecutor implements CommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommandExecutor.class);
    /**
     * Command Line executor
     */
    protected CommandLineExecutor commandLineExecutor = new CommandLineExecutor();
    /**
     * The timeout for the process in milliseconds
     */
    protected long timeout = Long.MAX_VALUE;
    /**
     * Command log consumer
     */
    protected CommandLogHandler commandLogHandler;

    /**
     * The user who executed the command
     */
    protected String user;

    private boolean sourceRc;

    public AbstractCommandExecutor() {
    }

    protected CommandLine wrappedRemoteCommandByUser(CommandLine commandLine, String user) {
        if (user != null && !user.isEmpty()) {
            return new CommandLine(Constants.Command.SUDO)
                    .addArgument("-iu")
                    .addArgument(user)
                    .addArguments(commandLine.toStrings(), false);
        }
        return commandLine;
    }

    protected CommandLine wrappedSourceFile(CommandLine commandLine) {
        return new CommandLine(Constants.Command.SOURCE)
                .addArgument("~/.bashrc")
                .addArgument(Constants.SEMI_COLON)
                .addArguments(commandLine.toStrings(), false);
    }
//
//    public AbstractCommandExecutor sourceRc(boolean sourceRc) {
//        this.sourceRc = sourceRc;
//        return this;
//    }

    /**
     * Set working directory
     *
     * @param workingDirectory command working directory
     * @return this
     */
    public AbstractCommandExecutor workingDirectory(String workingDirectory) {
        commandLineExecutor.setWorkDirectory(workingDirectory);
        return this;
    }

    public AbstractCommandExecutor commandUser(String user) {
        this.user = user;
        return this;
    }

    /**
     * @param commandLogHandler Set log handler to consume err & output log
     * @param logLevel          log level
     * @return this
     */
    @Override
    public AbstractCommandExecutor logHandler(CommandLogHandler commandLogHandler, int logLevel) {
        var streamHandler = new PumpStreamHandler(
                new LineOrientedOutputStream(commandLogHandler, logLevel, LogType.OUTPUT),
                new LineOrientedOutputStream(commandLogHandler, logLevel, LogType.ERROR));
        commandLineExecutor.setExecuteStreamHandler(streamHandler);
        this.commandLogHandler = commandLogHandler;
        return this;
    }

    /**
     * Set command execute timeout
     *
     * @param timeout timeout(ms)
     * @return this
     */
    @Override
    public AbstractCommandExecutor timeout(long timeout) {
        if (timeout != 0) {
            this.timeout = timeout;
        }
        return this;
    }

    /**
     * Create script file and execute script with work dir
     *
     * @param scriptSaveName script save name
     * @param script         script content
     * @return execute result
     */
    @Override
    public ExecResult execScript(String script, String... scriptSaveName) {
        String fileName = Optional.ofNullable(scriptSaveName)
                .filter(i -> i.length > 0)
                .map(i -> i[0]).orElse(System.currentTimeMillis() + "");
        var scriptFilePath = writeScriptToFile(fileName, script);
        return exec(CommandLine.parse(Constants.Command.SH).addArgument(scriptFilePath));
    }

    /**
     * Write script file to local
     *
     * @param name   script file name
     * @param script script file content
     * @return script file path
     */
    protected String writeScriptToFile(String name, String script) {
        if (name == null || name.isEmpty()) {
            name = "" + System.currentTimeMillis();
        }
        String scriptFilePath = "/tmp/script/" + name;
        try {
            FileUtils.writeStringToFile(new File(scriptFilePath), script);
            LOGGER.info("Write script file to local {}", scriptFilePath);
        } catch (IOException e) {
            LOGGER.error("Write script file to local {} error", scriptFilePath, e);
        }

        log("[CREATE_SH_FILE] Write script file to " + scriptFilePath + " success");
        return scriptFilePath;
    }

    protected void log(String line) {
        if (commandLogHandler != null) {
            commandLogHandler.consume(line, 999, LogType.OUTPUT);
        }
    }

    /**
     * Execute string command
     *
     * @param commandLine string command
     * @return execute result
     */
    @Override
    public ExecResult exec(String commandLine) {
        if (commandLine == null || commandLine.isEmpty()) {
            throw new IllegalArgumentException("Illegal command line " + commandLine);
        }
        boolean local = getClass().equals(DefaultCommandExecutor.class);
        if (local) {
            String[] split = commandLine.split(Constants.SEMI_COLON);
            List<String> commandLines = Arrays.stream(split).map(String::trim).filter(i -> !i.isEmpty()).collect(Collectors.toList());
            if (commandLines.size() > 1) {
                throw new IllegalArgumentException("Include multiline lines, please use the method execScript");
            }
            commandLine = commandLines.get(0);
        }
        return exec(CommandLine.parse(commandLine));
    }

    /**
     * Execute command line
     *
     * @param commandLine commandline
     * @return execute result
     */
    @Override
    public ExecResult exec(CommandLine commandLine) {
        commandLine = wrappedRemoteCommandByUser(commandLine, user);
        if (sourceRc) {
            commandLine = wrappedSourceFile(commandLine);
        }
        //        if (commandLogHandler != null && commandLogHandler instanceof Closeable) {
//            try {
//                ((Closeable) commandLogHandler).close();
//            } catch (IOException e) {
//                LOGGER.error("Log handler close error", e);
//            }
//        }
        return commandLineExecutor
                .exec(commandLine, timeout);
    }

    /**
     * Stop executor
     */
    @Override
    public boolean stop() {
        if (commandLineExecutor.isKilled()) {
            return true;
        }
        if (commandLineExecutor != null) {
            commandLineExecutor.kill();
            return commandLineExecutor.isKilled();
        }
        return false;
    }

    @Override
    public CommandLogHandler getLogHandler() {
        return commandLogHandler;
    }

    /**
     * Uses the default level of 999.
     *
     * @param commandLogHandler log handler
     * @return this
     */
    @Override
    public AbstractCommandExecutor logHandler(CommandLogHandler commandLogHandler) {
        return logHandler(commandLogHandler, 999);
    }

    /**
     * Log line consumer
     */
    static class LineOrientedOutputStream extends LogOutputStream {
        private final CommandLogHandler commandLogHandler;
        private final LogType logType;

        public LineOrientedOutputStream(CommandLogHandler commandLogHandler, int logLevel, LogType logType) {
            super(logLevel);
            this.commandLogHandler = commandLogHandler;
            this.logType = logType;
        }

        @Override
        protected void processLine(String line, int logLevel) {
            commandLogHandler.consume(line, logLevel, logType);
        }
    }
}
