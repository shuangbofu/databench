package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.databench.common.enums.JobHistoryStatus;
import org.example.databench.persistence.entity.base.BizEntity;

/**
 * Created by shuangbofu on 2022/3/31 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("`job_history`")
@Accessors(chain = true)
public class JobHistory extends BizEntity {
    private String jobId;
    private Boolean done;
    private Long fileId;
    private JobHistoryStatus status;
}
