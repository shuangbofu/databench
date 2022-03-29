package org.example.databench.service;

import org.example.databench.persistence.entity.FileCommit;
import org.example.databench.service.base.BaseService;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 6:44 下午
 */
public interface FileCommitService extends BaseService<FileCommit> {
    long getCountByFileId(Long fileId);

    List<FileCommit> getListByFileId(Long fileId);

    boolean existCommit(Long fileId, Integer version);
}
