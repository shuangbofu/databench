package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.enums.FileCategory;
import org.example.databench.common.enums.ModuleType;
import org.example.databench.persistence.entity.base.BizEntity;

/**
 * Created by shuangbofu on 2021/9/9 3:25 下午
 */
@EqualsAndHashCode(callSuper = true)
@TableName("`folder`")
@Data
public class Folder extends BizEntity {
    private String name;
    private ModuleType belong;
    private FileCategory category;
    private Long parentId;
}
