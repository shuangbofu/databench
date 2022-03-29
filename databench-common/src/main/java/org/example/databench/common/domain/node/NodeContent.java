package org.example.databench.common.domain.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.domain.file.FileContent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shuangbofu on 2021/9/11 3:05 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NodeContent extends FileContent {
    private Long nodeId;
    private String nodeType;
    private Map<String, Object> vars = new HashMap<>();

    public NodeContent() {

    }
}
