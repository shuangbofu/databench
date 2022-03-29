package org.example.databench.persistence.entity.base;

import io.github.shuangbofu.helper.annotation.LoadDaoHook;
import io.github.shuangbofu.helper.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by shuangbofu on 2021/9/10 9:46 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@LoadDaoHook(UserHook.class)
public class UserEntity extends BaseEntity {
    private String createBy;
    private String modifiedBy;
    private String tenant;
}
