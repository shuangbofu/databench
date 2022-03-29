package org.example.databench.service.impl;

import org.example.databench.persistence.dao.FileCommitDao;
import org.example.databench.persistence.entity.FileCommit;
import org.example.databench.service.FileCommitService;
import org.example.databench.service.base.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 6:45 下午
 */
@Service
public class FileCommitServiceImpl extends CommonService<FileCommit, FileCommitDao> implements FileCommitService {

    public FileCommitServiceImpl(@Autowired FileCommitDao dao) {
        super(dao);
    }

    @Override
    public long getCountByFileId(Long fileId) {
        return countBy(q -> q.lambda().eq(FileCommit::getFileId, fileId));
    }

    @Override
    public List<FileCommit> getListByFileId(Long fileId) {
        return selectListBy(q -> q.lambda().eq(FileCommit::getFileId, fileId));
    }

    @Override
    public boolean existCommit(Long fileId, Integer version) {
        return countBy(q -> q.lambda().eq(FileCommit::getFileId, fileId)
                .eq(FileCommit::getVersion, version)) > 0;
    }
}
