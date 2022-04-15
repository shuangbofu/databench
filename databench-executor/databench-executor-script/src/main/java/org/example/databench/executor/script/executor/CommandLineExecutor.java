package org.example.databench.executor.script.executor;

import org.apache.commons.exec.*;
import org.example.databench.executor.script.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The main execute command line executor
 */
public class CommandLineExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineExecutor.class);
    /**
     * Danger command array
     */
    private static final String[][] DEFAULT_FORBID_COMMANDS = {
            {"rm", "-rf", "*"},
            {"rm", "-r", "-f", "*"},
            {"mv", "/dev/null"},
            {">", " /dev/sda"},
            {"wget", "-O-", "|", "sh"},
            {"mkfs.ext3", "/dev/sda"}
    };
    private String workDirectory;
    /**
     * Execute to handle input and output stream of subprocesses.
     */
    private ExecuteStreamHandler executeStreamHandler;
    /**
     * The main abstraction to start an external process
     */
    private Executor executor;

    public CommandLineExecutor() {
    }

    public CommandLineExecutor setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
        return this;
    }

    public CommandLineExecutor setExecuteStreamHandler(ExecuteStreamHandler executeStreamHandler) {
        this.executeStreamHandler = executeStreamHandler;
        return this;
    }

    /**
     * Execute the command line
     *
     * @param commandLine command line
     * @return execute result
     */
    public ExecResult exec(CommandLine commandLine) {
        var MAX_TIME_OUT = Long.MAX_VALUE;
        return exec(commandLine, MAX_TIME_OUT);
    }

    /**
     * Execute the command line with timeout option
     *
     * @param commandLine command line
     * @param timeout     timeout(ms)
     * @return execute result
     */
    public ExecResult exec(CommandLine commandLine, long timeout) {
        int exitValue;
        var cmd = commandLine.toString();
        LOGGER.info("Execute command {}", cmd);
        String[] commandLines = commandLine.toStrings();
        if (commandLines.length == 0) {
            return ExecResult.error(-1, new ExecuteException("Command cannot be empty", -1));
        }
        String sudoUser = null;
        if (Constants.Command.SUDO.equals(Arrays.asList(commandLines).get(0))) {
            if (commandLines.length > 2) {
                sudoUser = Arrays.asList(commandLines).get(2);
            }
        }
        var watchdog = new ExecuteWatchdog(timeout, sudoUser);
        try {
            checkCommandLine(commandLine);
            executor = new DefaultExecutor();
            if (workDirectory != null && workDirectory.length() != 0) {
                executor.setWorkingDirectory(new File(workDirectory));
            }
            executor.setWatchdog(watchdog);
            if (executeStreamHandler != null) {
                executor.setStreamHandler(executeStreamHandler);
            }
            executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
            exitValue = executor.execute(commandLine);
            if (exitValue == 0) {
                return ExecResult.success(exitValue);
            }
            return ExecResult.error(exitValue, null);
        } catch (ExecuteException e) {
            int exitVal = e.getExitValue();
            LOGGER.error("Can not run command {}, {}", cmd, timeout, e);
            if (watchdog.isTimeout()) {
                LOGGER.info("Command execute timeout {}", cmd);
                return ExecResult.error(exitVal,
                        new ExecuteException("Command execute timeout," + timeout, e.getExitValue()));
            }
            if (exitVal == 143) {
                LOGGER.info("Command received a SIGTERM, interrupt {}", cmd, e);
                return ExecResult.interrupt(exitVal, e);
            }
            return ExecResult.error(exitVal, e);
        } catch (Exception e) {
            LOGGER.error("Can not run command {}", cmd, e);
            return ExecResult.error(-1, e);
        }
    }

    /**
     * Check the command line for danger
     *
     * @param commandLine command line
     */
    private void checkCommandLine(CommandLine commandLine) throws ExecuteException {
        // TODO More complex checks are needed.
        var lines = Arrays.asList(commandLine.toStrings());
        var commands = Arrays.stream(DEFAULT_FORBID_COMMANDS)
                .map(Arrays::asList).collect(Collectors.toList());
        boolean error = commands.stream().anyMatch(lines::containsAll);
        if (error) {
            throw new ExecuteException("Danger command line", -999);
        }
    }

    /**
     * Kill the command line
     */
    public void kill() {
        if (executor != null && !executor.getWatchdog().killedProcess()) {
            executor.getWatchdog().destroyProcess();
        }
    }

    /**
     * Killed or not
     *
     * @return boolean is killed
     */
    public boolean isKilled() {
        if (executor != null) {
            return executor.getWatchdog().killedProcess();
        }
        return false;
    }
}
