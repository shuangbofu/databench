package org.example.databench.starter;
/**
 * Created by shuangbofu on 2021/9/20 1:56 下午
 */

import com.google.common.collect.Lists;
import org.example.databench.common.domain.node.NodeCfg;
import org.example.databench.common.domain.node.Output;
import org.example.databench.common.enums.SourceType;
import org.example.databench.persistence.dao.FileDao;
import org.example.databench.persistence.dao.FileVersionDao;
import org.example.databench.persistence.dao.OutputNodeDao;
import org.example.databench.persistence.dao.WorkspaceDao;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.persistence.entity.OutputNode;
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
public class TestOutputNode {

    @Autowired
    private OutputNodeDao outputNodeDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private FileVersionDao fileVersionDao;
    @Autowired
    private WorkspaceDao workspaceDao;

    @Test
    public void test() {
        OutputNode rootNode = new OutputNode();
        rootNode.setFileId(0L);
        rootNode.setName("test_root");
        outputNodeDao.insert(rootNode);
        Lists.newArrayList(1L, 3L, 5L).forEach(j -> {
            File i = fileDao.selectOneById(j);
            String workspaceName = workspaceDao.selectValueById(Workspace::getName, i.getWorkspaceId());
            OutputNode outputNode = new OutputNode();
            outputNode.setFileId(j);
            outputNode.setName(workspaceName + "." + i.getId() + "_out");
            outputNodeDao.insert(outputNode);
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
        List<Output> outputs = Arrays.stream(parents).map(i -> {
            OutputNode node = outputNodeDao.selectOneBy(q -> q.lambda().eq(OutputNode::getFileId, i));
            Output output = new Output();
            output.setName(node.getName());
            output.setSource(SourceType.manual_record);
            return output;
        }).collect(Collectors.toList());
        NodeCfg cfg = (NodeCfg) fileVersion.getCfg();
        cfg.setInputs(outputs);
        fileVersion.setCfg(cfg);
//        fileVersionDao.
//                updateBy(q -> q.lambda()
//                        .set(FileVersion::getCfg, (FileCfg) cfg)
//                        .eq(FileVersion::getFileId, file.getId())
//                        .eq(FileVersion::getVersion, file.getVersion()));
        fileVersionDao.updateEntityById(fileVersion);
    }
}
