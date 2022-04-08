package org.example.databench.service.impl;

import org.example.databench.persistence.dao.NodeOutputDao;
import org.example.databench.persistence.entity.NodeOutput;
import org.example.databench.service.NodeOutputService;
import org.example.databench.service.base.CommonService;
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
    public List<NodeOutput> getOutputNodes() {
        return selectAll();
    }

    @Override
    public List<NodeOutput> getOutputNodesByFileId(Long fileId) {
        return selectListBy(q -> q.lambda().eq(NodeOutput::getFileId, fileId));
    }

    @Override
    public List<NodeOutput> getOutputNodesByRefFileId(Long fileId) {
        return selectListBy(q -> q.lambda().apply(String.format("find_in_set('%s',ref_file_ids)", fileId)));
    }

    @Override
    public List<Long> getOutputRefFileIds(String name) {
        return getDao().selectValueBy(NodeOutput::getRefFileIds,
                q -> q.lambda().eq(NodeOutput::getName, name)
        );
    }

    @Override
    public boolean updateOutputRefFileIds(String name, List<Long> fileIds) {
        NodeOutput nodeOutput = new NodeOutput();
        nodeOutput.setRefFileIds(fileIds);
        return getDao().updateEntityBy(nodeOutput, q -> q.lambda()
                .eq(NodeOutput::getName, name)) > 0;
    }
}
