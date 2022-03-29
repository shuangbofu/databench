package org.example.databench.service;

import org.example.databench.common.enums.FileType;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.persistence.entity.File;
import org.example.databench.service.base.BaseService;
import org.example.databench.service.domain.param.FileParam;
import org.example.databench.service.domain.vo.FileVO;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 1:40 下午
 */
public interface FileService extends BaseService<File> {

    boolean modifyFile(FileParam fileParam);

    List<FileVO> listFiles(ModuleType belong, FileType fileType);

    List<FileVO> listDeletedFiles();

    boolean deleteFile(Long fileId);

    Long getCountByFolderId(Long folderId);

    boolean updateVersion(Long fileId, Integer version);
}
