package org.example.databench.service;

import org.example.databench.persistence.entity.Workspace;
import org.example.databench.service.base.BaseService;
import org.example.databench.service.domain.vo.WorkspaceVO;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/20 10:02 下午
 */
public interface WorkspaceService extends BaseService<Workspace> {
    String getNameById(Long id);

    Long getIdByName(String name);

    WorkspaceVO getWorkspace(String name);

    List<WorkspaceVO> getWorkspaceList();
}
