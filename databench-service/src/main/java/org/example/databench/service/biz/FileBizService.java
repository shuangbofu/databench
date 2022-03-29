package org.example.databench.service.biz;

import org.example.databench.service.domain.param.CommitParam;
import org.example.databench.service.domain.param.FileModifyParam;
import org.example.databench.service.domain.param.FileParam;
import org.example.databench.service.domain.param.FolderParam;
import org.example.databench.service.domain.vo.CommitVersionVO;
import org.example.databench.service.domain.vo.FileDetailVO;
import org.example.databench.service.domain.vo.FileVO;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 1:49 下午
 */
public interface FileBizService {

    FileVO createFile(FileParam fileParam);

    Long cloneFile(Long originFileId, String name);

    FileDetailVO getFileDetail(Long fileId);

    boolean deleteFile(Long fileId);

    Long createFolder(FolderParam folderParam);

    boolean deleteFolder(Long folderId);

    boolean modifyFile(FileModifyParam fileParam);

    boolean commitFile(CommitParam commitParam);

    boolean commitFileCheck(CommitParam commitParam);

    List<CommitVersionVO> getCommitVersions(Long fileId);

    List<FileVO> getUniCommittedFiles();
}
