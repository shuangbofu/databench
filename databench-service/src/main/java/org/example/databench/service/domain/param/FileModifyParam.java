package org.example.databench.service.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.domain.file.FileBase;
import org.example.databench.common.domain.file.FileCfg;

/**
 * Created by shuangbofu on 2021/9/11 8:58 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileModifyParam extends FileParam {
    private FileBase content;
    private FileCfg cfg;
}
