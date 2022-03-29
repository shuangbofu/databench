package org.example.databench.service.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.domain.file.FileBase;
import org.example.databench.service.domain.node.Graph;

/**
 * Created by shuangbofu on 2021/9/21 10:16 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileNodeVO extends FileBase implements Graph.INode {
    private Long id;
}
