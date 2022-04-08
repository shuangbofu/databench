package org.example.databench.service;

import org.example.databench.common.vo.PageVO;
import org.example.databench.persistence.entity.Node;
import org.example.databench.service.base.BaseService;
import org.example.databench.service.domain.param.NodeFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.NodeVO;

/**
 * Created by shuangbofu on 2021/9/12 12:23 上午
 */
public interface NodeService extends BaseService<Node> {
    Node getNodeByFileId(Long fileId);

    boolean updateVersion(Long nodeId, Integer version, String name, boolean isProd);

    Long getNodeIdByFileId(Long fileId);

    Long getWorkspaceNodeId(Long workspaceId);

    PageVO<NodeVO> getNodePage(PageFilterParam<NodeFilter> param);
}
