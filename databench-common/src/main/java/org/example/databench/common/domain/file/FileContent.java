package org.example.databench.common.domain.file;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by shuangbofu on 2022/3/29 00:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileContent extends FileBase {
    private String description = "";
    private String code = "";
}
