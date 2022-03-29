package org.example.databench.service;

import org.example.databench.common.enums.ModuleType;
import org.example.databench.persistence.entity.Folder;
import org.example.databench.service.base.BaseService;
import org.example.databench.service.domain.base.FolderVO;
import org.example.databench.service.domain.param.FolderParam;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/10 11:04 下午
 */
public interface FolderService extends BaseService<Folder> {

    Long addFolder(FolderParam folderParam);

    /**
     * change parentId or name
     *
     * @param folderParam folder param
     * @return vo
     */
    boolean modifyFolder(FolderParam folderParam);

    List<FolderVO> getFolders(ModuleType belong);

    long getChildCount(Long folderId);
}
