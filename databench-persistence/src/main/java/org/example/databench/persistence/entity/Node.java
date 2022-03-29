package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.persistence.entity.base.BizEntity;

/**
 * Created by shuangbofu on 2021/9/9 7:31 下午
 */
@TableName("`node`")
@EqualsAndHashCode(callSuper = true)
@Data
public class Node extends BizEntity {
    private String name;
    private String nodeType;
    private Long fileId;
    private Integer devVersion = -1;
    private Integer prodVersion = -1;
    private String owner;
}
