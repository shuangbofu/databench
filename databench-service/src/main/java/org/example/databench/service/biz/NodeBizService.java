package org.example.databench.service.biz;

import org.example.databench.common.domain.node.Output;
import org.example.databench.common.enums.SourceType;
import org.example.databench.common.utils.Pair;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.service.domain.node.Graph;
import org.example.databench.service.domain.vo.NodeVO;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/12 1:06 上午
 */
public interface NodeBizService {
    boolean commitNode(File file, FileVersion fileVersion);

    void checkDependNodes(List<Output> inputs);

    boolean createOutputNode(Long fileId, String name, String tableName, SourceType source);

    Pair<Integer, Integer> getExecNodeVersion(Long fileId);

    Graph<NodeVO> getNodeGraph(Long nodeId, Integer depth, boolean parent, boolean prod);
}
