package org.example.databench.service;

import org.example.databench.persistence.entity.Workspace;
import org.example.databench.service.base.BaseService;

/**
 * Created by shuangbofu on 2021/9/20 10:02 下午
 */
public interface WorkspaceService extends BaseService<Workspace> {
    String getNameById(Long id);

    Long getIdByName(String name);
}
