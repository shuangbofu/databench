package org.example.databench.service.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.enums.FileCategory;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.service.domain.base.BizBaseParam;

/**
 * Created by shuangbofu on 2021/9/10 11:06 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FolderParam extends BizBaseParam {
    private String name;
    private ModuleType belong;
    private FileCategory category;
    private Long parentId;
}
