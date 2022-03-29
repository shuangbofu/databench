package org.example.databench.persistence.entity.base;

import io.github.shuangbofu.helper.annotation.LoadDaoHook;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by shuangbofu on 2021/9/10 10:22 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@LoadDaoHook(BizHook.class)
public class BizEntity extends WorkspaceEntity {
    private Long bizId;
}
