package org.example.databench.service.domain.vo;

import lombok.Data;
import org.example.databench.common.enums.JobHistoryStatus;

/**
 * Created by shuangbofu on 2022/3/31 17:16
 */
@Data
public class JobHistoryVO {
    private String jobId;
    private Boolean done;
    private JobHistoryStatus status;
    private Long fileId;
    private Long createTime;
    private String createBy;
}
