package org.example.databench.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.persistence.entity.base.WorkspaceEntity;

/**
 * Created by shuangbofu on 2021/9/9 12:04 下午
 */
@EqualsAndHashCode(callSuper = true)
@TableName("`biz`")
@Data
public class Biz extends WorkspaceEntity {
    private String name;
    private String description;
    private String layoutCfg = "{}";
    private String cfg = "{}";
    private String dagGraph = "{}";
}
