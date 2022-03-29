package org.example.databench.persistence.dao;

import io.github.shuangbofu.helper.dao.BaseDao;
import org.example.databench.persistence.dao.mapper.NodeMapper;
import org.example.databench.persistence.entity.Node;

/**
 * Created by shuangbofu on 2021/9/12 12:22 上午
 */
public interface NodeDao extends BaseDao<Node, NodeMapper> {
}
