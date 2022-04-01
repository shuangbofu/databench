package org.example.databench.executor.script.log;

public interface CommandLogHandler {
    /**
     * Consume command output & error log line
     *
     * @param line     log line
     * @param logLevel level
     * @param logType  type error / output
     */
    void consume(String line, int logLevel, LogType logType);
}
