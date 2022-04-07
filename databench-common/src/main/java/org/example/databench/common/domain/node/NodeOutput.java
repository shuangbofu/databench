package org.example.databench.common.domain.node;

import lombok.Data;
import org.example.databench.common.enums.SourceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangbofu on 2021/9/20 5:15 下午
 */
@Data
public class NodeOutput implements Serializable {
    private String name;
    private String tableName;
    private SourceType source;
    private List<OutputNode> outputNodes = new ArrayList<>();
}
