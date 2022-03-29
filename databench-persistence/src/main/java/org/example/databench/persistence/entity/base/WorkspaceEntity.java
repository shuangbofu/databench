package org.example.databench.persistence.entity.base;

import io.github.shuangbofu.helper.annotation.LoadDaoHook;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by shuangbofu on 2021/9/10 10:21 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@LoadDaoHook(WorkspaceHook.class)
public class WorkspaceEntity extends UserEntity {
    private Long workspaceId;
}
