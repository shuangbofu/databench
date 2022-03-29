package org.example.databench.service.impl;

import org.example.databench.persistence.dao.NodeDepDao;
import org.example.databench.persistence.entity.NodeDep;
import org.example.databench.service.NodeDepService;
import org.example.databench.service.base.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/21 9:38 上午
 */
@Service
public class NodeDepServiceImpl extends CommonService<NodeDep, NodeDepDao> implements NodeDepService {
    public NodeDepServiceImpl(@Autowired NodeDepDao dao) {
        super(dao);
    }

    @Transactional
    @Override
    public boolean replaceParentNodeDeps(Long nodeId, List<Long> parentIds, boolean isProd) {
        getDao().deleteBy(q -> q.lambda().eq(NodeDep::getChildId, nodeId).eq(NodeDep::isProd, isProd));
        parentIds.forEach(i -> insert(new NodeDep(i, nodeId, isProd)));
        return true;
    }

    @Override
    public List<Long> getRefNodes(Long nodeId, boolean isProd, boolean parent) {
        return getDao().selectValueListBy(
                parent ? NodeDep::getParentId : NodeDep::getChildId,
                q -> q.lambda().eq(NodeDep::isProd, isProd)
                        .eq(parent ? NodeDep::getChildId : NodeDep::getParentId, nodeId)
        );
    }
}
