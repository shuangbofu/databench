package org.example.databench.service.domain.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by shuangbofu on 2021/9/10 11:09 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizBaseParam extends WorkspaceBaseParam {
    private Long bizId;
}
