package org.example.databench.service.domain.param;

import lombok.Data;

/**
 * Created by shuangbofu on 2022/3/31 17:19
 */
@Data
public class JobHistoryFilter {
    private Long workspaceId;
    private Long bizId;
    private Long fileId;
}
