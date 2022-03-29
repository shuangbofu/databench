package org.example.databench.service.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.enums.FileCategory;
import org.example.databench.common.enums.FileType;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.service.domain.base.BizBaseParam;

/**
 * Created by shuangbofu on 2021/9/11 1:50 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileParam extends BizBaseParam {
    private String name;
    private ModuleType belong;
    private FileType fileType;
    private Long folderId;

    public FileCategory getCategory() {
        return fileType.getCategory();
    }
}
