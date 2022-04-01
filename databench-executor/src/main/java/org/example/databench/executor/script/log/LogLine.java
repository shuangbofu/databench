package org.example.databench.executor.script.log;

public class LogLine {
    private String content;
    private LogType logType;
    private int logLevel;

    public LogLine() {
    }

    public LogLine(String content, LogType logType, int logLevel) {
        this.content = content;
        this.logType = logType;
        this.logLevel = logLevel;
    }

    public String getContent() {
        return content;
    }

    public LogType getLogType() {
        return logType;
    }

    public int getLogLevel() {
        return logLevel;
    }
}
