package org.example.databench.starter;
/**
 * Created by shuangbofu on 2021/9/20 1:56 下午
 */

import com.google.common.collect.Lists;
import org.example.databench.common.domain.node.NodeCfg;
import org.example.databench.common.enums.SourceType;
import org.example.databench.persistence.dao.FileDao;
import org.example.databench.persistence.dao.FileVersionDao;
import org.example.databench.persistence.dao.NodeOutputDao;
import org.example.databench.persistence.dao.WorkspaceDao;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.persistence.entity.NodeOutput;
import org.example.databench.persistence.entity.Workspace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestNodeOutputNode {

    @Autowired
    private NodeOutputDao nodeOutputDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private FileVersionDao fileVersionDao;
    @Autowired
    private WorkspaceDao workspaceDao;

    @Test
    public void test() {
        NodeOutput rootNode = new NodeOutput();
        rootNode.setFileId(0L);
        rootNode.setName("test_root");
        nodeOutputDao.insert(rootNode);
        Lists.newArrayList(1L, 3L, 5L).forEach(j -> {
            File i = fileDao.selectOneById(j);
            String workspaceName = workspaceDao.selectValueById(Workspace::getName, i.getWorkspaceId());
            NodeOutput nodeOutput = new NodeOutput();
            nodeOutput.setFileId(j);
            nodeOutput.setName(workspaceName + "." + i.getId() + "_out");
            nodeOutputDao.insert(nodeOutput);
        });
    }

    @Test
    public void test2() {
        setDep(3L, 1L);
        setDep(5L, 1L, 3L);
    }

    private void setDep(Long id, Long... parents) {
        File file = fileDao.selectOneById(id);
        FileVersion fileVersion = fileVersionDao.selectOneBy(q -> q.lambda()
                .eq(FileVersion::getFileId, file.getId())
                .eq(FileVersion::getVersion, file.getVersion())
        );
        List<org.example.databench.common.domain.node.NodeOutput> nodeOutputs = Arrays.stream(parents).map(i -> {
            NodeOutput node = nodeOutputDao.selectOneBy(q -> q.lambda().eq(NodeOutput::getFileId, i));
            org.example.databench.common.domain.node.NodeOutput nodeOutput = new org.example.databench.common.domain.node.NodeOutput();
            nodeOutput.setName(node.getName());
            nodeOutput.setSource(SourceType.manual_record);
            return nodeOutput;
        }).collect(Collectors.toList());
        NodeCfg cfg = (NodeCfg) fileVersion.getCfg();
        cfg.setInputs(nodeOutputs);
        fileVersion.setCfg(cfg);
//        fileVersionDao.
//                updateBy(q -> q.lambda()
//                        .set(FileVersion::getCfg, (FileCfg) cfg)
//                        .eq(FileVersion::getFileId, file.getId())
//                        .eq(FileVersion::getVersion, file.getVersion()));
        fileVersionDao.updateEntityById(fileVersion);
    }
}
