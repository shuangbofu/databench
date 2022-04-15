package org.example.executor.api.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2022/3/31 16:28
 */
@Data
public class Log {
    private List<String> lines;
    private boolean done;
}
