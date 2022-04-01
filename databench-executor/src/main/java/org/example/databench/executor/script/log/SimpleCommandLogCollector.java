package org.example.databench.executor.script.log;

/**
 * Log collector
 */
public class SimpleCommandLogCollector implements CommandLogHandler {

    private final StringBuilder out;
    private final StringBuilder err;

    public SimpleCommandLogCollector() {
        out = new StringBuilder();
        err = new StringBuilder();
    }

    @Override
    public void consume(String line, int level, LogType logType) {
        if (LogType.OUTPUT.equals(logType)) {
            out.append("\n").append(line);
        } else {
            err.append("\n").append(line);
        }
    }

    public String getErr() {
        return removeLastLine(err);
    }

    public String getOut() {
        var out = removeLastLine(this.out);
        if (out.length() == 0) {
            return getErr();
        }
        return out;
    }

    private String removeLastLine(StringBuilder builder) {
        var length = builder.length();
        return length > 0 ? builder.substring(1, length) : "";
    }
}
