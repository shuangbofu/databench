package org.example.databench.service.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.databench.common.domain.file.FileTuple;
import org.example.databench.common.enums.CommitType;

/**
 * Created by shuangbofu on 2021/9/12 10:08 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommitVersionVO extends FileTuple {
    private Long id;
    private CommitType commitType;
    private String comment;
    private String createBy;
    private String modifiedBy;
    private Long createTime;
    private Long updateTime;
    private Integer version;
    private boolean inDev;
    private boolean inProd;
}
