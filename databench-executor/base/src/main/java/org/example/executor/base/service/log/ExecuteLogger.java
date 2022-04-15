package org.example.executor.base.service.log;

public interface ExecuteLogger {
    void warn(String msg);

    void error(String msg);

    void info(String msg);

    void error(String msg, Throwable e);
}
