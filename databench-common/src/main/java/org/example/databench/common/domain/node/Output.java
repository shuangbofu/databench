package org.example.databench.common.domain.node;

import lombok.Data;
import org.example.databench.common.enums.SourceType;

import java.io.Serializable;

/**
 * Created by shuangbofu on 2021/9/20 5:15 下午
 */
@Data
public class Output implements Serializable {
    private String name;
    private SourceType source;
}
