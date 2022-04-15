package org.example.executor.base.service.log;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shuangbofu on 2022/4/15 22:15
 */
public class MemoLogStore implements ExecuteLogger {
    private final List<String> logLines = new ArrayList<>();
    private final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MemoLogStore() {
    }

    public List<String> getLogLines() {
        return logLines;
    }

    private void log(LogType logType, String msg) {
        msg = SIMPLE_DATE_FORMAT.format(new Date()) + " [" + logType + "] " + msg;
        logLines.add(msg);
    }

    private void log(LogType logType, String msg, Throwable e) {
        log(logType, msg + "\n" + ExceptionUtils.getStackTrace(e));
    }

    @Override
    public void warn(String msg) {
        log(LogType.WARN, msg);
    }

    @Override
    public void error(String msg) {
        log(LogType.ERROR, msg);
    }

    @Override
    public void info(String msg) {
        log(LogType.INFO, msg);
    }

    @Override
    public void error(String msg, Throwable e) {
        log(LogType.ERROR, msg, e);
    }
}
