package org.example.databench.common.enums;

import lombok.Getter;

/**
 * Created by shuangbofu on 2021/9/10 9:42 下午
 */
@Getter
public enum FileCategory {
    bigdata("大数据"),
    common("通用"),
    database("数据库"),
    custom("自定义"),

    file("文件"),
    datasource("数据源"),

    undefined("未定义"),
    ;


    private final String desc;

    FileCategory(String desc) {
        this.desc = desc;
    }
}
