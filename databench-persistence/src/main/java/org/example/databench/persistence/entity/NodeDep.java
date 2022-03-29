package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.shuangbofu.helper.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by shuangbofu on 2021/9/21 9:35 上午
 */
@EqualsAndHashCode(callSuper = true)
@TableName("`node_dep`")
@Data
public class NodeDep extends BaseEntity {
    private Long parentId;
    private Long childId;
    private boolean prod;

    public NodeDep(Long parentId, Long childId, boolean prod) {
        this.parentId = parentId;
        this.childId = childId;
        this.prod = prod;
    }

    public NodeDep() {
    }
}
