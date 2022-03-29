package org.example.databench.service.domain.vo;

import lombok.Data;

/**
 * Created by shuangbofu on 2021/9/11 9:35 下午
 */
@Data
public class BizVO {
    private Long id;
    private String name;
    private String description;
    private String createBy;
    private String modifiedBy;
    private Long createTime;
    private Long updateTime;
}
