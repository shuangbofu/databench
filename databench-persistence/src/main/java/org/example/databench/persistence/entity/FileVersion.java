package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.shuangbofu.helper.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.domain.file.FileBase;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.persistence.handler.FileCfgJsonHandler;
import org.example.databench.persistence.handler.FileContentJsonHandler;

/**
 * Created by shuangbofu on 2021/9/11 2:25 下午
 */
@TableName(value = "`file_version`", autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class FileVersion extends BaseEntity {
    private Long fileId;
    private Integer version;

    @TableField(typeHandler = FileContentJsonHandler.class)
    private FileBase content;
    @TableField(typeHandler = FileCfgJsonHandler.class)
    private FileCfg cfg;
}
