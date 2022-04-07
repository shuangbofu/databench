package org.example.databench.service.biz.impl;

import org.example.databench.common.domain.node.NodeCfg;
import org.example.databench.common.enums.SourceType;
import org.example.databench.common.utils.Pair;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.persistence.entity.Node;
import org.example.databench.persistence.entity.NodeOutput;
import org.example.databench.service.NodeDepService;
import org.example.databench.service.NodeOutputService;
import org.example.databench.service.NodeService;
import org.example.databench.service.WorkspaceService;
import org.example.databench.service.base.AbstractService;
import org.example.databench.service.biz.NodeBizService;
import org.example.databench.service.domain.node.Edge;
import org.example.databench.service.domain.node.Graph;
import org.example.databench.service.domain.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2021/9/12 1:06 上午
 */
@Service
public class NodeBizServiceImpl extends AbstractService implements NodeBizService {

    @Autowired
    private NodeOutputService nodeOutputService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeDepService nodeDepService;
    @Autowired
    private WorkspaceService workspaceService;

    @Transactional
    @Override
    public boolean commitNode(File file, FileVersion fileVersion) {
        NodeCfg cfg = (NodeCfg) fileVersion.getCfg();
        checkDependNodes(cfg.getInputs());
        Function<Node, Node> copyToNode = node -> {
            copyAToBWithoutId(file, node, (f, n) -> {
                n.setFileId(f.getId());
                n.setNodeType(f.getFileType().name());
                n.setDevVersion(fileVersion.getVersion());
            });
            return copyAToBWithoutId(fileVersion, node);
        };

        // create/update node version
        Node node = nodeService.getNodeByFileId(file.getId());
        if (node == null) {
            node = new Node();
            nodeService.insertAny(copyToNode.apply(node));
        } else {
            nodeService.updateVersion(node.getId(), fileVersion.getVersion(), false);
        }

        Map<String, Optional<Long>> map = getInputsNodeIds(cfg.getInputs());
        List<Long> parentNodeIds = map.values().stream().filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());
        nodeDepService.replaceParentNodeDeps(node.getId(), parentNodeIds, false);
        return true;
    }

    @Override
    public void checkDependNodes(List<org.example.databench.common.domain.node.NodeOutput> inputs) {
        Map<String, Optional<Long>> map = getInputsNodeIds(inputs);
        List<String> unCommitNode = map.keySet().stream().filter(i -> map.get(i).isEmpty())
                .collect(Collectors.toList());
        if (!unCommitNode.isEmpty()) {
            throw new RuntimeException("上游依赖输出" + String.join(",", unCommitNode) + "未提交");
        }
    }

    private Map<String, Optional<Long>> getInputsNodeIds(List<org.example.databench.common.domain.node.NodeOutput> inputs) {
        return inputs.stream().collect(Collectors.toMap(org.example.databench.common.domain.node.NodeOutput::getName,
                i -> getNodeIdByOutputName(i.getName())));
    }

    private Optional<Long> getNodeIdByOutputName(String name) {
        if (name.endsWith("_root")) {
            return Optional.ofNullable(workspaceService.getIdByName(name.substring(0, name.length() - 5)));
        } else {
            Long fileId = nodeOutputService.getFileIdByOutputName(name);
            return Optional.ofNullable(nodeService.getNodeIdByFileId(fileId));
        }
    }

    @Override
    public Optional<Node> getNodeByOutputName(String name) {
        return getNodeIdByOutputName(name).map(i -> nodeService.selectOneById(i));
    }

    @Override
    public boolean createOutputNode(Long fileId, String name, String tableName, SourceType source) {
        NodeOutput nodeOutput = new NodeOutput();
        nodeOutput.setFileId(fileId);
        nodeOutput.setName(name);
        nodeOutput.setSource(source);
        nodeOutput.setTableName(tableName);
        return nodeOutputService.insertAny(nodeOutput);
    }

    @Override
    public Pair<Integer, Integer> getExecNodeVersion(Long fileId) {
        var node = nodeService.getNodeByFileId(fileId);
        if (node == null) {
            return new Pair<>(0, 0);
        }
        return new Pair<>(node.getDevVersion(), node.getProdVersion());
    }

    @Override
    public Graph<NodeVO> getNodeGraph(Long nodeId, Integer depth, boolean parent, boolean prod) {
        return setUpGraph(new Graph<>(), depth, parent, prod, nodeId);
    }

    private Graph<NodeVO> setUpGraph(Graph<NodeVO> graph, Integer depth, boolean parent, boolean prod, Long nodeId) {
        if (depth > 0) {
            if (graph.getNodes().stream().noneMatch(i -> i.getId().equals(nodeId))) {
                graph.getNodes().add(aToB(nodeService.selectOneById(nodeId), NodeVO.class, (t, v) -> {
                    v.setVersion(prod ? t.getProdVersion() : t.getDevVersion());
                }));
            }
            List<Long> refNodes = nodeDepService.getRefNodes(nodeId, prod, parent);
            refNodes.forEach(i -> {
                graph.getEdges().add(new Edge(parent ? i : nodeId, parent ? nodeId : i));
                setUpGraph(graph, depth - 1, parent, prod, i);
            });
        }
        return graph;
    }
}
