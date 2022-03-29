package org.example.databench.service.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.service.domain.base.BizBaseParam;

/**
 * Created by shuangbofu on 2021/9/12 12:49 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommitParam extends BizBaseParam {
    private String comment;
    private Long fileId;
}
