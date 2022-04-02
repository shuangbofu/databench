package org.example.databench.service.impl;

import org.example.databench.common.enums.FileType;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.persistence.dao.FileDao;
import org.example.databench.persistence.entity.File;
import org.example.databench.service.FileService;
import org.example.databench.service.base.CommonService;
import org.example.databench.service.domain.param.FileParam;
import org.example.databench.service.domain.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 1:41 下午
 */
@Service
public class FileServiceImpl extends CommonService<File, FileDao> implements FileService {

    public FileServiceImpl(@Autowired FileDao dao) {
        super(dao);
    }

    @Override
    public FileType getFileType(Long fileId) {
        return getDao().selectValueById(File::getFileType, fileId);
    }

    @Override
    public boolean modifyFile(FileParam fileParam) {
        return updateById(fileParam.getId(),
                q -> q.lambda()
                        .set(File::getFolderId, fileParam.getFolderId())
                        .set(File::getName, fileParam.getName())
                        .eq(File::getId, fileParam.getId())
        );
    }

    @Override
    public List<FileVO> listFiles(ModuleType belong, FileType fileType) {
        List<File> files = selectListBy(i -> i.lambda()
                .eq(File::getBelong, belong)
                .eq(File::getDeleteFlag, false)
                .eq(fileType != null, File::getFileType, fileType)
        );
        return listAToListB(files, FileVO.class);
    }

    @Override
    public List<FileVO> listDeletedFiles() {
        return listAToListB(selectListBy(i -> i.lambda().eq(File::getDeleteFlag, true)), FileVO.class);
    }

    @Override
    public boolean deleteFile(Long fileId) {
        return updateById(fileId, i -> i.lambda().set(File::getDeleteFlag, true));
    }

    @Override
    public Long getCountByFolderId(Long folderId) {
        return countBy(i -> i.lambda().eq(File::getFolderId, folderId));
    }

    @Override
    public boolean updateVersion(Long fileId, Integer version) {
        return updateById(fileId, q -> q.lambda().set(File::getVersion, version));
    }

    @Override
    public String getName(Long fileId) {
        return getDao().selectValueById(File::getName, fileId);
    }
}
