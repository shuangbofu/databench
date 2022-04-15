package org.example.databench.service.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.domain.file.FileTuple;
import org.example.databench.common.enums.ModuleType;

/**
 * Created by shuangbofu on 2021/9/11 3:09 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileDetailVO extends FileTuple {
    private ModuleType moduleType;
}
