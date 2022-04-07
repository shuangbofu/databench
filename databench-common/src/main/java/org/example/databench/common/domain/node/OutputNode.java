package org.example.databench.common.domain.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by shuangbofu on 2022/4/6 17:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputNode {
    private Long id;
    private String name;
    private String owner;
}
