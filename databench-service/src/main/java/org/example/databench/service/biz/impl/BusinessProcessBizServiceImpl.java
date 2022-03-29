package org.example.databench.service.biz.impl;

import org.example.databench.common.domain.node.NodeCfg;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.service.BizService;
import org.example.databench.service.FileService;
import org.example.databench.service.FileVersionService;
import org.example.databench.service.OutputNodeService;
import org.example.databench.service.base.AbstractService;
import org.example.databench.service.biz.BusinessProcessBizService;
import org.example.databench.service.domain.node.Edge;
import org.example.databench.service.domain.node.Graph;
import org.example.databench.service.domain.vo.FileNodeVO;
import org.example.databench.service.domain.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2021/9/11 9:35 下午
 */
@Service
public class BusinessProcessBizServiceImpl extends AbstractService implements BusinessProcessBizService {
    @Autowired
    private BizService bizService;
    @Autowired
    private FileVersionService fileVersionService;
    @Autowired
    private FileService fileDao;
    @Autowired
    private OutputNodeService outputNodeService;

    @Override
    public Graph<FileNodeVO> drawBizGraph() {
        List<FileVO> files = fileDao.listFiles(ModuleType.development, null);
        Set<FileNodeVO> nodes = new HashSet<>();
        Set<Edge> edges = new HashSet<>();
        files.stream().filter(i -> i.getFileType().haveDag())
                .forEach(i -> {
                    Long fileId = i.getId();
                    NodeCfg cfg = (NodeCfg) fileVersionService.getCfgByFileIdAndVersion(fileId, i.getVersion());
                    List<Long> parentFileIds = cfg.getInputs().stream().map(j ->
                            outputNodeService.getFileIdByOutputName(j.getName())).collect(Collectors.toList());
                    nodes.add(aToB(i, FileNodeVO.class));
                    parentFileIds.forEach(k -> edges.add(new Edge(k, fileId)));
                });
        return new Graph<>(new HashSet<>(nodes), edges);
    }
}
