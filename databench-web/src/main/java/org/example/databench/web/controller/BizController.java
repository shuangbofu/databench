package org.example.databench.web.controller;

import org.example.databench.service.BizService;
import org.example.databench.service.biz.BusinessProcessBizService;
import org.example.databench.service.domain.node.Graph;
import org.example.databench.service.domain.param.BizParam;
import org.example.databench.service.domain.vo.BizVO;
import org.example.databench.service.domain.vo.FileNodeVO;
import org.example.databench.web.annotations.ResultController;
import org.example.databench.web.config.DaoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 9:37 下午
 */
@ResultController("api/biz")
public class BizController extends BaseController {

    @Autowired
    private BizService bizService;
    @Autowired
    private BusinessProcessBizService businessProcessBizService;

    public BizController(@Autowired DaoContext daoContext) {
        super(daoContext);
    }

    @GetMapping("list")
    public List<BizVO> getAllBizs(Long workspaceId) {
        workspaceContext(workspaceId);
        return bizService.selectAllBizs();
    }

    @PostMapping
    public Long addBiz(@RequestBody BizParam bizParam) {
        return bizService.addBiz(bizParam);
    }

    @GetMapping("graph")
    public Graph<FileNodeVO> getBizDagGraph(Long bizId) {
        bizContext(bizId);
        return businessProcessBizService.drawBizGraph();
    }
}
