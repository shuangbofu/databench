package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.enums.CommitType;
import org.example.databench.persistence.entity.base.UserEntity;

/**
 * Created by shuangbofu on 2021/9/11 4:38 下午
 */
@TableName("`file_commit`")
@EqualsAndHashCode(callSuper = true)
@Data
public class FileCommit extends UserEntity {
    private Integer version;
    private Long fileId;
    private CommitType commitType;
    private boolean published;
    private String comment;
}
