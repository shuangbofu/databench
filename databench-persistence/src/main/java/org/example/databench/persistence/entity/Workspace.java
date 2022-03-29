package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.persistence.entity.base.UserEntity;

/**
 * Created by shuangbofu on 2021/9/9 3:27 下午
 */
@EqualsAndHashCode(callSuper = true)
@TableName("`workspace`")
@Data
public class Workspace extends UserEntity {
    private String name;
    private String spaceCfg;
}
