package org.example.databench.web.controller;

import org.example.databench.web.config.DaoContext;

/**
 * Created by shuangbofu on 2021/9/11 1:16 下午
 */
public abstract class BaseController {
    private final DaoContext daoContext;

    public BaseController(DaoContext daoContext) {
        this.daoContext = daoContext;
    }

    protected void daoContext(Long workspaceId, Long bizId) {
        daoContext.setWorkspaceId(workspaceId);
        daoContext.setBizId(bizId);
    }

    protected void bizContext(Long bizId) {
        daoContext(0L, bizId);
    }

    protected void workspaceContext(Long workspaceId) {
        daoContext(workspaceId, 0L);
    }
}
