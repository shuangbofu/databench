package org.example.databench.executor.script.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * consumer log system printer
 */
public class SystemOut implements CommandLogHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemOut.class);
    public static SystemOut INSTANCE = new SystemOut();

    @Override
    public void consume(String line, int level, LogType logType) {
        (LogType.OUTPUT.equals(logType) ? System.out : System.err).println(line);
    }
}
