package org.example.databench.common.domain.node;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by shuangbofu on 2022/4/6 17:26
 */
@Data
@NoArgsConstructor
public class OutputNode {
    private Long id;
    private String name;
    private String fileName;
    private String owner;
}
