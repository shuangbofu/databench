package org.example.databench.service.domain.param;

import lombok.Data;

/**
 * Created by shuangbofu on 2021/9/21 10:56 上午
 */
@Data
public class PageFilterParam<T> {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private T filter;
}
