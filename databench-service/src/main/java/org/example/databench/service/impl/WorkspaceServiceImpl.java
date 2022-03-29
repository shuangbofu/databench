package org.example.databench.service.impl;

import org.example.databench.persistence.dao.WorkspaceDao;
import org.example.databench.persistence.entity.Workspace;
import org.example.databench.service.WorkspaceService;
import org.example.databench.service.base.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shuangbofu on 2021/9/20 10:03 下午
 */
@Service
public class WorkspaceServiceImpl extends CommonService<Workspace, WorkspaceDao> implements WorkspaceService {
    public WorkspaceServiceImpl(@Autowired WorkspaceDao dao) {
        super(dao);
    }

    @Override
    public String getNameById(Long id) {
        return getDao().selectValueById(Workspace::getName, id);
    }

    @Override
    public Long getIdByName(String name) {
        return getDao().selectValueBy(Workspace::getId, q -> q.lambda().eq(Workspace::getName, name));
    }
}
