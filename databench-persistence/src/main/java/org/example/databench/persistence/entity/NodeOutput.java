package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.shuangbofu.helper.mybatis.handler.LongListTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.enums.SourceType;
import org.example.databench.persistence.entity.base.UserEntity;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/20 1:35 下午
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "`output`", autoResultMap = true)
@Data
public class NodeOutput extends UserEntity {
    private Long fileId;
    private String name;
    private String tableName;
    private SourceType source;
    @TableField(value = "ref_file_ids", typeHandler = LongListTypeHandler.class)
    private List<Long> refFileIds;
}
