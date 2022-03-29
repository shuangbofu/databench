package org.example.databench.service.impl;

import org.example.databench.common.domain.file.FileBase;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.persistence.dao.FileVersionDao;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.service.FileVersionService;
import org.example.databench.service.base.CommonService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by shuangbofu on 2021/9/11 2:32 下午
 */
@Service
public class FileVersionServiceImpl extends CommonService<FileVersion, FileVersionDao> implements FileVersionService {

    public FileVersionServiceImpl(FileVersionDao dao) {
        super(dao);
    }

    @Override
    public FileVersion getByFileIdAndVersion(Long fileId, Integer version) {
        return selectOneBy(q -> q.lambda().eq(FileVersion::getFileId, fileId)
                .eq(FileVersion::getVersion, version));
    }

    @Override
    public FileCfg getCfgByFileIdAndVersion(Long fileId, Integer version) {
        return getDao().selectValueBy(FileVersion::getCfg,
                q -> q.lambda().eq(FileVersion::getFileId, fileId)
                        .eq(FileVersion::getVersion, version));
    }

    @Override
    public boolean updateContentAndCfgByFileIdAndVersion(Long fileId, Integer version,
                                                         FileBase content, FileCfg cfg) {
        // 通过条件update不支持typeHandler,改为使用updateEntity
//        return updateBy(q -> q.lambda()
//                .set(FileVersion::getContent, content)
//                .set(FileVersion::getCfg, cfg)
//                .eq(FileVersion::getFileId, fileId)
//                .eq(FileVersion::getVersion, version)
//        );
        FileVersion fileVersion = new FileVersion();
        fileVersion.setContent(content);
        fileVersion.setCfg(cfg);
        return getDao().updateEntityBy(fileVersion, q -> q.lambda().eq(FileVersion::getFileId, fileId)
                .eq(FileVersion::getVersion, version)) > 0;
    }

    @Override
    public boolean getIsChangedByFileIdAndVersion(Long fileId, Integer version) {
        FileVersion fileVersion = getDao()
                .selectOneBy(q -> q.lambda().select(FileVersion::getCreateTime, FileVersion::getUpdateTime));
        return !Objects.equals(fileVersion.getCreateTime(), fileVersion.getUpdateTime());
    }

    @Override
    public Integer getLastVersion(Long fileId) {
        return getDao().selectValueBy(FileVersion::getVersion,
                q -> q.lambda().orderByDesc(FileVersion::getVersion)
                        .eq(FileVersion::getFileId, fileId)
                        .last("limit 1")
        );
    }
}
