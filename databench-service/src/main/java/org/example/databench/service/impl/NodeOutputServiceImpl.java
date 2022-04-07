package org.example.databench.service.impl;

import org.example.databench.persistence.dao.NodeOutputDao;
import org.example.databench.persistence.entity.NodeOutput;
import org.example.databench.service.NodeOutputService;
import org.example.databench.service.base.CommonService;
import org.example.databench.service.domain.vo.OutputNodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/20 5:43 下午
 */
@Service
public class NodeOutputServiceImpl extends CommonService<NodeOutput, NodeOutputDao> implements NodeOutputService {

    public NodeOutputServiceImpl(@Autowired NodeOutputDao dao) {
        super(dao);
    }

    @Override
    public Long getFileIdByOutputName(String name) {
        return getDao().selectValueBy(NodeOutput::getFileId,
                q -> q.lambda().eq(NodeOutput::getName, name));
    }

    @Override
    public List<OutputNodeVO> getOutputNodes() {
        return listAToListB(selectAll(), OutputNodeVO.class);
    }

    @Override
    public List<NodeOutput> getOutputNodesByFileId(Long fileId) {
        return selectListBy(q -> q.lambda().eq(NodeOutput::getFileId, fileId));
    }
}
