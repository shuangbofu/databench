package org.example.databench.web.controller;

import org.example.databench.common.vo.PageVO;
import org.example.databench.service.NodeOutputService;
import org.example.databench.service.NodeService;
import org.example.databench.service.biz.NodeBizService;
import org.example.databench.service.domain.node.Graph;
import org.example.databench.service.domain.param.NodeFilter;
import org.example.databench.service.domain.param.PageFilterParam;
import org.example.databench.service.domain.vo.NodeVO;
import org.example.databench.service.domain.vo.OutputNodeVO;
import org.example.databench.web.annotations.ResultController;
import org.example.databench.web.config.DaoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/12 12:45 上午
 */
@ResultController("api/node")
public class NodeController extends BaseController {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeBizService nodeBizService;
    @Autowired
    private NodeOutputService nodeOutputService;

    public NodeController(@Autowired DaoContext daoContext) {
        super(daoContext);
    }

    @GetMapping("output")
    public List<OutputNodeVO> getOutputNodes() {
        return nodeBizService.getOutputNodes();
    }

    @PostMapping("page")
    public PageVO<NodeVO> getNodePage(@RequestParam("workspaceId") Long workspaceId,
                                      @RequestBody PageFilterParam<NodeFilter> param) {
        workspaceContext(workspaceId);
        return nodeService.getNodePage(param);
    }

    @GetMapping("/graph")
    public Graph<NodeVO> getNodeGraph(Long nodeId, Integer depth, boolean parent, boolean prod) {
        return nodeBizService.getNodeGraph(nodeId, depth, parent, prod);
    }
}
