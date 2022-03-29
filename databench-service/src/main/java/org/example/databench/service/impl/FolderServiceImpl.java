package org.example.databench.service.impl;

import org.example.databench.common.enums.ModuleType;
import org.example.databench.persistence.dao.FolderDao;
import org.example.databench.persistence.entity.Folder;
import org.example.databench.service.FolderService;
import org.example.databench.service.base.CommonService;
import org.example.databench.service.domain.base.FolderVO;
import org.example.databench.service.domain.param.FolderParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/10 11:04 下午
 */
@Service
public class FolderServiceImpl extends CommonService<Folder, FolderDao> implements FolderService {

    public FolderServiceImpl(@Autowired FolderDao dao) {
        super(dao);
    }

    @Override
    public Long addFolder(FolderParam folderParam) {
        return insertAnyRtId(folderParam);
    }

    @Override
    public boolean modifyFolder(FolderParam folderParam) {
        return updateBy(i -> i.lambda()
                .set(Folder::getParentId, folderParam.getParentId())
                .set(Folder::getName, folderParam.getName())
                .eq(Folder::getId, folderParam.getId())
        );
    }

    @Override
    public List<FolderVO> getFolders(ModuleType belong) {
        return listAToListB(selectListBy(q -> q.lambda()
                        .eq(Folder::getBelong, belong)),
                FolderVO.class);
    }

    @Override
    public long getChildCount(Long folderId) {
        return countBy(i -> i.lambda().eq(Folder::getParentId, folderId));
    }
}
