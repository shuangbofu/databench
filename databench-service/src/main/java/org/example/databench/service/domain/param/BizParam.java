package org.example.databench.service.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.service.domain.base.WorkspaceBaseParam;

/**
 * Created by shuangbofu on 2021/9/11 9:41 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizParam extends WorkspaceBaseParam {
    private String name;
    private String description;
}
