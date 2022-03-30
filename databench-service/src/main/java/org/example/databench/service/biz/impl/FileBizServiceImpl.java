package org.example.databench.service.biz.impl;

import com.google.common.collect.Lists;
import org.example.databench.common.domain.file.*;
import org.example.databench.common.domain.file.datasource.DatasourceParam;
import org.example.databench.common.domain.file.datasource.JdbcParam;
import org.example.databench.common.domain.node.NodeCfg;
import org.example.databench.common.domain.node.NodeContent;
import org.example.databench.common.domain.query.QueryResult;
import org.example.databench.common.domain.resource.FunctionContent;
import org.example.databench.common.domain.resource.ResourceContent;
import org.example.databench.common.enums.*;
import org.example.databench.common.utils.Pair;
import org.example.databench.persistence.entity.File;
import org.example.databench.persistence.entity.FileCommit;
import org.example.databench.persistence.entity.FileVersion;
import org.example.databench.persistence.entity.Folder;
import org.example.databench.service.*;
import org.example.databench.service.base.AbstractService;
import org.example.databench.service.biz.FileBizService;
import org.example.databench.service.biz.NodeBizService;
import org.example.databench.service.domain.param.CommitParam;
import org.example.databench.service.domain.param.FileModifyParam;
import org.example.databench.service.domain.param.FileParam;
import org.example.databench.service.domain.param.FolderParam;
import org.example.databench.service.domain.vo.CommitVersionVO;
import org.example.databench.service.domain.vo.FileDetailVO;
import org.example.databench.service.domain.vo.FileVO;
import org.example.databench.service.domain.vo.JobResultVO;
import org.example.databench.service.manager.ExecutorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2021/9/11 1:49 下午
 */
@Service
public class FileBizServiceImpl extends AbstractService implements FileBizService {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileVersionService fileVersionService;
    @Autowired
    private FileCommitService fileCommitService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private NodeBizService nodeBizService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private ExecutorManager executorManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileVO createFile(FileParam fileParam) {
        if (fileParam.getFolderId() > 0) {
            Folder folder = folderService.selectOneById(fileParam.getFolderId());
            fileParam.setBelong(folder.getBelong());
            fileParam.setBizId(folder.getBizId());
            fileParam.setWorkspaceId(folder.getWorkspaceId());
        }
        File file = fileService.insertAnyRtEntity(fileParam, (a, f) -> {
        });
        fileVersionService.insertAnyRtEntity(fileParam, (i, fileVersion) -> {
            fileVersion.setFileId(file.getId());
            FileTuple fileTuple = setUpFileTuple(file);
            fileVersion.setContent(fileTuple.getContent());
            fileVersion.setCfg(fileTuple.getCfg());
        });
        String workspaceName = workspaceService.getNameById(file.getWorkspaceId());

        nodeBizService.createOutputNode(
                file.getId(),
                workspaceName + "." + file.getName() + "_out",
                "", SourceType.system);
        return aToB(file, FileVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long cloneFile(Long originFileId, String name) {
        File cloneFile = fileService.selectOneOrThrow(originFileId);
        cloneFile.setName(name);
        cloneFile.setId(null);
        Long newFieldId = fileService.insertAnyRtId(cloneFile);
        FileVersion fileVersion = fileVersionService
                .getByFileIdAndVersion(originFileId, cloneFile.getVersion());
        fileVersion.setId(null);
        fileVersion.setFileId(newFieldId);
        fileVersionService.insertAny(fileVersion);
        return newFieldId;
    }

    public Pair<File, FileVersion> getFileAndVersion(Long fileId) {
        File file = fileService.selectOneOrThrow(fileId);
        Integer version = file.getVersion();
        FileVersion fileVersion = fileVersionService.getByFileIdAndVersion(fileId, version);
        return new Pair<>(file, fileVersion);
    }

    @Override
    public FileDetailVO getFileDetail(Long fileId) {
        Pair<File, FileVersion> pair = getFileAndVersion(fileId);
        File file = pair.left;
        FileVersion fileVersion = pair.right;
        copyAToB(file, fileVersion.getContent());
        if (fileVersion.getContent() instanceof NodeContent) {
            Long nodeId = nodeService.getNodeIdByFileId(fileId);
            ((NodeContent) fileVersion.getContent()).setNodeId(nodeId);
        }
        return aToB(fileVersion, FileDetailVO.class);
    }

    @Transactional(noRollbackFor = Exception.class)
    @Override
    public boolean deleteFile(Long fileId) {
        commitFile(fileId, "被删除", CommitType.discard);
        fileService.deleteFile(fileId);
        return true;
    }

    private void commitFile(Long fileId, String comment, CommitType commitType) {
        File file = fileService.selectOneOrThrow(fileId);
        FileCommit fileCommit = copyAToBWithoutId(file, new FileCommit(), (a, b) -> {
            b.setCommitType(commitType);
            b.setComment(comment);
            b.setFileId(fileId);
        });
        fileCommitService.insertAny(fileCommit);
    }

    @Override
    public Long createFolder(FolderParam folderParam) {
        if (folderParam.getParentId() > 0) {
            Folder folder = folderService.selectOneById(folderParam.getParentId());
            folderParam.setBelong(folder.getBelong());
            folderParam.setBizId(folder.getBizId());
            folderParam.setWorkspaceId(folder.getWorkspaceId());
        }
        return folderService.addFolder(folderParam);
    }

    @Override
    public boolean deleteFolder(Long folderId) {
        Consumer<Supplier<Long>> check = countSupplier -> Optional.of(countSupplier.get())
                .filter(i -> i == 0)
                .orElseThrow(() -> new RuntimeException("存在子结点无法删除"));
        check.accept(() -> folderService.getChildCount(folderId));
        check.accept(() -> fileService.getCountByFolderId(folderId));
        return true;
    }

    @Override
    public boolean modifyFile(FileModifyParam fileParam) {
        File file = fileService.selectOneOrThrow(fileParam.getId());

        FileBase content = fileParam.getContent();
        FileCfg cfg = fileParam.getCfg();
        if (content == null && cfg == null) {
            fileService.modifyFile(fileParam);
        }
        checkContentAndCfg(content, cfg);
        if (cfg != null) {
            // TODO modify biz dag graph
            if (cfg instanceof QueryCfg) {
                Long datasourceFileId = ((QueryCfg) cfg).getDatasourceFileId();
                Integer lastVersion = fileVersionService.getLastVersion(datasourceFileId);
                ((QueryCfg) cfg).setDatasourceFileVersion(lastVersion);
            }
        }

        fileVersionService.updateContentAndCfgByFileIdAndVersion(
                file.getId(), file.getVersion(),
                content, cfg);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean commitFile(CommitParam commitParam) {
        Long fileId = commitParam.getFileId();
        Pair<File, FileVersion> pair = getFileAndVersion(fileId);
        FileVersion fileVersion = pair.right;
        File file = pair.left;

        long count = fileCommitService.getCountByFileId(fileId);
        CommitType commitType = count == 0 ? CommitType.add : CommitType.modify;
        commitFile(fileId, commitParam.getComment(), commitType);

        // 开发提交到node
        if (ModuleType.development.equals(file.getBelong())) {
            nodeBizService.commitNode(file, fileVersion);
        }

        int newVersion = fileVersion.getVersion() + 1;
        fileVersion.setVersion(newVersion);
        fileVersion.setId(null);

        fileVersionService.insertAny(fileVersion);
        fileService.updateVersion(fileId, newVersion);
        return true;
    }

    @Override
    public boolean commitFileCheck(CommitParam commitParam) {
        // TODO
        Pair<File, FileVersion> pair = getFileAndVersion(commitParam.getFileId());
        NodeCfg cfg = (NodeCfg) pair.right.getCfg();
        nodeBizService.checkDependNodes(cfg.getInputs());
        return false;
    }

    @Override
    public List<CommitVersionVO> getCommitVersions(Long fileId) {
        List<FileCommit> fileCommits = fileCommitService.getListByFileId(fileId);
        if (fileCommits.size() == 0) {
            return Lists.newArrayList();
        }
        Pair<Integer, Integer> versionPair = nodeBizService.getExecNodeVersion(fileId);
        Integer devVersion = versionPair.left;
        Integer prodVersion = versionPair.right;
        return fileCommits.stream()
                .map(i -> aToB(fileVersionService.getByFileIdAndVersion(fileId, i.getVersion()), CommitVersionVO.class, (t, v) -> {
                    v.setInProd(prodVersion != null && prodVersion.equals(v.getVersion()));
                    v.setInDev(devVersion != null && devVersion.equals(v.getVersion()));
                }))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileVO> getUniCommittedFiles() {
        List<FileVO> files = fileService.listFiles(ModuleType.development, null);
        return files.stream().filter(i -> {
            Integer version = i.getVersion();
            boolean changed = fileVersionService.getIsChangedByFileIdAndVersion(i.getId(), version);
            boolean exist = fileCommitService.existCommit(i.getId(), version);
            return !exist && changed;
        }).collect(Collectors.toList());
    }

    @Override
    public JobResultVO runFile(Long fileId, FileTuple fileTuple) {
        if (fileId != null) {
            fileTuple = getFileDetail(fileId);
        }
        if (fileTuple == null) {
            // TODO
            throw new RuntimeException("error");
        }
        return runFile(fileTuple);
    }

    private void checkContentAndCfg(FileBase content, FileCfg cfg) {
        // TODO
    }

    private FileTuple setUpFileTuple(File file) {
        FileType fileType = file.getFileType();
        FileBase content = null;
        FileCfg cfg = null;
        if (fileType.isResource()) {
            content = new ResourceContent();
        } else if (fileType.equals(FileType.function)) {
            content = new FunctionContent();
        }

        if (file.getBelong().equals(ModuleType.development)) {
            String nodeType = fileType.name();
            NodeContent nodeContent = new NodeContent();
            // TODO set default vars
            nodeContent.setVars(new HashMap<>());
            nodeContent.setFileType(fileType);
            nodeContent.setNodeType(nodeType);
            content = nodeContent;
        }

        if (content == null) {
            content = new FileContent();
            content.setFileType(fileType);
        }

        if (file.getBelong().isDevelop()) {
            cfg = new NodeCfg();
        } else if (file.getBelong().equals(ModuleType.query)) {
            cfg = new QueryCfg();
        } else if (file.getBelong().equals(ModuleType.api)) {
            cfg = new ApiCfg();
        } else if (file.getBelong().equals(ModuleType.resource)) {
            if (fileType.getCategory().equals(FileCategory.datasource)) {
                DatasourceParam datasourceParam;
                if (fileType.isJdbcResourceFile()) {
                    datasourceParam = new JdbcParam();
                } else {
                    datasourceParam = new DatasourceParam();
                }
                cfg = new DatasourceCfg(datasourceParam);
            }
        }
        if (cfg == null) {
            cfg = new FileCfg.EmptyFiLeCfg();
        }
        return new FileTuple(content, cfg);
    }

    public JobResultVO runFile(FileTuple fileTuple) {
        if (fileTuple.getContent().getBelong().equals(ModuleType.query)) {
            FileContent content = (FileContent) fileTuple.getContent();
            String queryCode = content.getCode();
            QueryCfg cfg = (QueryCfg) fileTuple.getCfg();
            if (cfg.getDatasourceFileId() == 0) {
                throw new RuntimeException("没有选择数据源");
            }
            FileVersion datasourceFile = fileVersionService.getByFileIdAndVersion(cfg.getDatasourceFileId(),
                    cfg.getDatasourceFileVersion());
            DatasourceCfg datasourceCfg = (DatasourceCfg) datasourceFile.getCfg();
            QueryResult queryResult = executorManager.getDatasourceApi().queryResult(datasourceCfg,
                    datasourceFile.getContent().getFileType().toDsType(), queryCode);
            return new JobResultVO(queryResult, null);
        }
        throw new RuntimeException("Not supported");
    }
}
