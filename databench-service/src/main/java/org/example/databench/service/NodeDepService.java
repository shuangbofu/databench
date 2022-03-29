package org.example.databench.service;

import org.example.databench.persistence.entity.NodeDep;
import org.example.databench.service.base.BaseService;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/21 9:38 上午
 */
public interface NodeDepService extends BaseService<NodeDep> {

    boolean replaceParentNodeDeps(Long nodeId, List<Long> parentIds, boolean isProd);

    List<Long> getRefNodes(Long nodeId, boolean isProd, boolean parent);
}
