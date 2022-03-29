package org.example.databench.service.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.domain.file.FileBase;

/**
 * Created by shuangbofu on 2021/9/11 5:27 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileVO extends FileBase {
    private Long id;
    private Long folderId;
}
