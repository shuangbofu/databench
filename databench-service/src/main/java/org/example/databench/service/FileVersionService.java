package org.example.databench.service;

import org.example.databench.common.domain.file.FileBase;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.service.base.BaseService;

/**
 * Created by shuangbofu on 2021/9/11 2:32 下午
 */
public interface FileVersionService extends BaseService<FileVersion> {

    FileVersion getByFileIdAndVersion(Long fileId, Integer version);

    FileCfg getCfgByFileIdAndVersion(Long fileId, Integer version);

    boolean updateContentAndCfgByFileIdAndVersion(Long fileId, Integer version, FileBase content, FileCfg cfg);

    boolean getIsChangedByFileIdAndVersion(Long fileId, Integer version);

    Integer getLastVersion(Long fileId);
}
