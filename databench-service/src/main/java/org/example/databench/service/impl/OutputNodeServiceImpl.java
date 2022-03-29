package org.example.databench.service.impl;

import org.example.databench.persistence.dao.OutputNodeDao;
import org.example.databench.persistence.entity.OutputNode;
import org.example.databench.service.OutputNodeService;
import org.example.databench.service.base.CommonService;
import org.example.databench.service.domain.vo.OutputNodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/20 5:43 下午
 */
@Service
public class OutputNodeServiceImpl extends CommonService<OutputNode, OutputNodeDao> implements OutputNodeService {

    public OutputNodeServiceImpl(@Autowired OutputNodeDao dao) {
        super(dao);
    }

    @Override
    public Long getFileIdByOutputName(String name) {
        return getDao().selectValueBy(OutputNode::getFileId,
                q -> q.lambda().eq(OutputNode::getName, name));
    }

    @Override
    public List<OutputNodeVO> getOutputNodes() {
        return listAToListB(selectAll(), OutputNodeVO.class);
    }
}
