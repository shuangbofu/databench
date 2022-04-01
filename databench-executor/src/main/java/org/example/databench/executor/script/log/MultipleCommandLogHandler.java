package org.example.databench.executor.script.log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangbofu on 2021/8/3 20:12
 */
public class MultipleCommandLogHandler implements CommandLogHandler {

    private final List<CommandLogHandler> commandLogHandlerList = new ArrayList<>();

    public MultipleCommandLogHandler add(CommandLogHandler commandLogHandler) {
        commandLogHandlerList.add(commandLogHandler);
        return this;
    }

    public List<CommandLogHandler> getLogHandlerList() {
        return commandLogHandlerList;
    }

    @Override
    public void consume(String line, int logLevel, LogType logType) {
        commandLogHandlerList.forEach(i -> i.consume(line, logLevel, logType));
    }
}
