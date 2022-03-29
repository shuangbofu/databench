package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.enums.SourceType;
import org.example.databench.persistence.entity.base.UserEntity;

/**
 * Created by shuangbofu on 2021/9/20 1:35 下午
 */
@EqualsAndHashCode(callSuper = true)
@TableName("`output_node`")
@Data
public class OutputNode extends UserEntity {
    private Long fileId;
    private String name;
    private String tableName;
    private SourceType source;
}
