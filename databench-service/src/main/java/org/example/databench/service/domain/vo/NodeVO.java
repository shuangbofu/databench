package org.example.databench.service.domain.vo;

import lombok.Data;
import org.example.databench.service.domain.node.Graph;

/**
 * Created by shuangbofu on 2021/9/21 10:46 上午
 */
@Data
public class NodeVO implements Graph.INode {
    private Long id;
    private Integer version;
    private String name;
    private String nodeType;
    private Long fileId;
    private Long createTime;
    private Long updateTime;
    private String createBy;
    private String modifiedBy;
    private Long bizId;
}
