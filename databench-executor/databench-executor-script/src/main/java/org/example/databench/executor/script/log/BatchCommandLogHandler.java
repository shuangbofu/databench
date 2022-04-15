package org.example.databench.executor.script.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BatchCommandLogHandler implements CommandLogHandler, Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchCommandLogHandler.class);
    private final int flushRowNumber;
    private final LinkedList<LogLine> cache;
    private final AtomicInteger counter;

    public BatchCommandLogHandler(int rowNumber) {
        flushRowNumber = rowNumber;
        cache = new LinkedList<>();
        counter = new AtomicInteger(0);
    }

    @Override
    public void consume(String line, int logLevel, LogType logType) {
        if (counter.get() >= flushRowNumber) {
            flushLines();
        }
        cache.add(new LogLine(line, logType, logLevel));
        counter.incrementAndGet();
    }

    private void flushLines() {
        LinkedList<LogLine> lines = new LinkedList<>(cache);
        cache.clear();
        counter.set(0);
        consume(lines);
    }

    public abstract void consume(LinkedList<LogLine> lines);

    @Override
    public void close() throws IOException {
        if (!cache.isEmpty()) {
            flushLines();
        }
        LOGGER.info("Logger closed");
    }
}
