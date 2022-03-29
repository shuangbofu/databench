package org.example.databench.service.domain.param;

import lombok.Data;

/**
 * Created by shuangbofu on 2021/9/21 10:57 上午
 */
@Data
public class NodeFilter {
    private boolean prod;
    private String nodeType;
    private String nodeId;
}
