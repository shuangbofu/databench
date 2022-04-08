package org.example.databench.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.databench.common.vo.PageVO;
import org.example.databench.persistence.dao.NodeDao;
import org.example.databench.persistence.entity.Node;
import org.example.databench.service.NodeService;
import org.example.databench.service.base.CommonService;
import org.example.databench.service.domain.param.NodeFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2021/9/12 12:23 上午
 */
@Service
public class NodeServiceImpl extends CommonService<Node, NodeDao> implements NodeService {
    public NodeServiceImpl(@Autowired NodeDao dao) {
        super(dao);
    }

    @Override
    public Node getNodeByFileId(Long fileId) {
        return selectOneBy(q -> q.lambda().eq(Node::getFileId, fileId));
    }

    @Override
    public boolean updateVersion(Long nodeId, Integer version, String name, boolean isProd) {
        return updateById(nodeId, q -> q.lambda()
                .set(Node::getName, name)
                .set(isProd ? Node::getProdVersion : Node::getDevVersion, version));
    }

    @Override
    public Long getNodeIdByFileId(Long fileId) {
        return getDao().selectValueBy(Node::getId, q -> q.lambda().eq(Node::getFileId, fileId));
    }

    @Override
    public Long getWorkspaceNodeId(Long workspaceId) {
        return getDao().selectValueBy(Node::getId,
                q -> q.lambda().eq(Node::getFileId, -1).eq(Node::getWorkspaceId, workspaceId));
    }

    @Override
    public PageVO<NodeVO> getNodePage(PageFilterParam<NodeFilter> param) {
        NodeFilter filter = param.getFilter();
        return toVOPage(param.getPageNum(), param.getPageSize(), q -> {
            q.lambda().ne(filter.isProd() ? Node::getProdVersion : Node::getDevVersion, -1L);
            if (StringUtils.isNotEmpty(filter.getNodeId())) {
                q.lambda().like(Node::getId, "%" + filter.getNodeId() + "%");
            } else if (StringUtils.isNotEmpty(filter.getNodeType())) {
                q.lambda().eq(Node::getNodeType, filter.getNodeType());
            }
        }, NodeVO.class);
    }
}
