package org.example.databench.service.domain.base;

import lombok.Data;
import org.example.databench.common.enums.FileCategory;
import org.example.databench.common.enums.ModuleType;

/**
 * Created by shuangbofu on 2021/9/10 11:14 下午
 */
@Data
public class FolderVO {
    private Long id;
    private String name;
    private Long parentId;
    private ModuleType belong;
    private FileCategory category;
    private Long createTime;
    private Long updateTime;
}
