package org.example.databench.common.domain.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.common.enums.ScheduleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2021/9/11 2:57 下午
 */
@Data
public class NodeCfg implements FileCfg {
    private ScheduleType scheduleType = ScheduleType.normal;
    private String cronExpression = "00 04 00 * * ?";
    private Integer timeout = 60;
    private RetryCfg retryCfg = new RetryCfg();
    private boolean autoRetry = false;
    private Map<String, Object> args = new HashMap<>();
    private List<Output> inputs = new ArrayList<>();
    private List<Output> outputs = new ArrayList<>();

    @JsonCreator
    public NodeCfg() {
    }
}
