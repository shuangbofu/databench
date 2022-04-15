package org.example.databench.executor.api;

import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.common.domain.file.FileContent;
import org.example.databench.common.enums.FileType;
import org.example.databench.common.enums.JobHistoryStatus;
import org.example.databench.executor.domain.Log;

/**
 * Created by shuangbofu on 2022/3/31 16:22
 */
public interface JobApi {
    Log fetchOffsetLog(String jobId, Long offset, Long length);

    boolean isDone(String jobId);

    boolean cancel(String jobId);

    JobHistoryStatus getStatus(String jobId);

    String executeFileJob(FileType fileType, FileContent fileContent, FileCfg fileCfg);
}
