package org.example.databench.service.biz.impl;

import org.example.databench.common.domain.node.NodeCfg;
import org.example.databench.common.domain.node.Output;
import org.example.databench.common.enums.SourceType;
import org.example.databench.common.utils.Pair;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.persistence.entity.Node;
import org.example.databench.persistence.entity.OutputNode;
import org.example.databench.service.NodeDepService;
import org.example.databench.service.NodeService;
import org.example.databench.service.OutputNodeService;
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
    private OutputNodeService outputNodeService;
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

        Map<String, Optional<Long>> map = getOutputNameNodeIdMap(cfg.getInputs());
        List<Long> parentNodeIds = map.values().stream().filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());
        nodeDepService.replaceParentNodeDeps(node.getId(), parentNodeIds, false);
        return true;
    }

    @Override
    public void checkDependNodes(List<Output> inputs) {
        Map<String, Optional<Long>> map = getOutputNameNodeIdMap(inputs);
        List<String> unCommitNode = map.keySet().stream().filter(i -> map.get(i).isEmpty())
                .collect(Collectors.toList());
        if (!unCommitNode.isEmpty()) {
            throw new RuntimeException("上游依赖输出" + String.join(",", unCommitNode) + "未提交");
        }
    }

    private Map<String, Optional<Long>> getOutputNameNodeIdMap(List<Output> inputs) {
        return inputs.stream().collect(Collectors.toMap(Output::getName,
                i -> Optional.ofNullable(outputNodeService.getFileIdByOutputName(i.getName()))
                        .map(j -> {
                            if (j == -1) {
                                String root = i.getName().replace("_root", "");
                                Long id = workspaceService.getIdByName(root);
                                return nodeService.getWorkspaceNodeId(id);
                            } else {
                                return nodeService.getNodeIdByFileId(j);
                            }
                        })));
    }

    @Override
    public boolean createOutputNode(Long fileId, String name, String tableName, SourceType source) {
        OutputNode outputNode = new OutputNode();
        outputNode.setFileId(fileId);
        outputNode.setName(name);
        outputNode.setSource(source);
        outputNode.setTableName(tableName);
        return outputNodeService.insertAny(outputNode);
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
