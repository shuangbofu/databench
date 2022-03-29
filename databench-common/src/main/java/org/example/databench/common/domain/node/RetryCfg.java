package org.example.databench.common.domain.node;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by shuangbofu on 2021/9/11 2:59 下午
 */
@Data
public class RetryCfg implements Serializable {
    private Integer count = 2;
    private Integer interval = 2 * 60;
}
