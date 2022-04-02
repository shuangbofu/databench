package org.example.databench.web.controller;

import org.example.databench.service.WorkspaceService;
import org.example.databench.service.domain.vo.WorkspaceVO;
import org.example.databench.web.annotations.ResultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by shuangbofu on 2022/4/2 21:35
 */
@ResultController("/api/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping
    public WorkspaceVO getWorkspace(@RequestParam("name") String name) {
        return workspaceService.getWorkspace(name);
    }

    @GetMapping("/list")
    public List<WorkspaceVO> getWorkspaceList() {
        return workspaceService.getWorkspaceList();
    }
}
