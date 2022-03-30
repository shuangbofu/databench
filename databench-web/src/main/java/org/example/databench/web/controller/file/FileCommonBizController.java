package org.example.databench.web.controller.file;

import com.google.common.collect.Lists;
import org.example.databench.common.domain.file.FileTuple;
import org.example.databench.common.enums.FileType;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.service.FileService;
import org.example.databench.service.FolderService;
import org.example.databench.service.biz.FileBizService;
import org.example.databench.service.domain.base.FolderVO;
import org.example.databench.service.domain.param.CommitParam;
import org.example.databench.service.domain.param.FileModifyParam;
import org.example.databench.service.domain.param.FileParam;
import org.example.databench.service.domain.param.FolderParam;
import org.example.databench.service.domain.vo.CommitVersionVO;
import org.example.databench.service.domain.vo.FileDetailVO;
import org.example.databench.service.domain.vo.FileVO;
import org.example.databench.service.domain.vo.JobResultVO;
import org.example.databench.web.annotations.ResultController;
import org.example.databench.web.config.DaoContext;
import org.example.databench.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2021/9/11 1:45 下午
 */
@ResultController("api")
public class FileCommonBizController extends BaseController {

    @Autowired
    private FolderService folderService;
    @Autowired
    private FileService fileService;

    @Autowired
    private FileBizService fileBizService;

    public FileCommonBizController(@Autowired DaoContext daoContext) {
        super(daoContext);
    }

    @PostMapping("folder")
    public Long createFolder(@RequestBody FolderParam folderParam) {
        return fileBizService.createFolder(folderParam);
    }

    @PutMapping("folder")
    public boolean modifyFolder(@RequestBody FolderParam folderParam) {
        return folderService.modifyFolder(folderParam);
    }

    @DeleteMapping("folder")
    public boolean deleteFolder(Long folderId) {
        return fileBizService.deleteFolder(folderId);
    }

    @PostMapping("file")
    public FileVO createFile(@RequestBody FileParam fileParam) {
        return fileBizService.createFile(fileParam);
    }

    @GetMapping("file")
    public FileDetailVO getFileDetail(Long fileId) {
        return fileBizService.getFileDetail(fileId);
    }

    @GetMapping("file/deleteList")
    public List<FileVO> getDeletedFiles(Long workspaceId) {
        workspaceContext(workspaceId);
        return fileService.listDeletedFiles();
    }

    @PutMapping("file")
    public boolean modifyFile(@RequestBody FileParam fileParam) {
        return fileService.modifyFile(fileParam);
    }

    @PutMapping("file/detail")
    public boolean modifyFile(@RequestBody FileModifyParam fileParam) {
        return fileBizService.modifyFile(fileParam);
    }

    @DeleteMapping("file")
    public boolean deleteFile(Long fileId) {
        return fileBizService.deleteFile(fileId);
    }

    @PostMapping("file/clone")
    public Long cloneFile(@RequestBody FileParam fileParam) {
        return fileBizService.cloneFile(fileParam.getId(), fileParam.getName());
    }

    @PostMapping("file/commit")
    public boolean commitFile(@RequestBody CommitParam commitParam) {
        return fileBizService.commitFile(commitParam);
    }

    @PostMapping("file/commit/check")
    public boolean checkFileCheck(@RequestBody CommitParam commitParam) {
        return fileBizService.commitFileCheck(commitParam);
    }

    @GetMapping("file/commit/list")
    public List<CommitVersionVO> getCommitVersions(Long fileId) {
        return fileBizService.getCommitVersions(fileId);
    }

    @GetMapping("file/commit/ready")
    public List<FileVO> getUnCommittedFiles(Long bizId) {
        bizContext(bizId);
        return fileBizService.getUniCommittedFiles();
    }

    @GetMapping("tree")
    public Map<String, List<Object>> getTreeData(Long workspaceId, Long bizId, ModuleType belong) {
        daoContext(workspaceId, bizId);
        Map<String, List<Object>> treeData = new HashMap<>();
        List<FolderVO> folders = folderService.getFolders(belong);
        List<FileVO> files = fileService.listFiles(belong, null);
        treeData.put("folders", Lists.newArrayList(folders));
        treeData.put("files", Lists.newArrayList(files));
        return treeData;
    }

    @GetMapping("file/list")
    public List<FileVO> getFiles(Long workspaceId, Long bizId, ModuleType belong, FileType fileType) {
        daoContext(workspaceId, bizId);
        return fileService.listFiles(belong, fileType);
    }

    @PostMapping("file/run")
    public JobResultVO runFile(@RequestParam(value = "fileId", required = false) Long fileId,
                               @RequestBody(required = false) FileTuple fileTuple) {
        return fileBizService.runFile(fileId, fileTuple);
    }
}
