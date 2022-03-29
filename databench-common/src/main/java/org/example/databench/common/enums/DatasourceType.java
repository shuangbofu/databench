package org.example.databench.common.enums;

import com.google.common.collect.Lists;

/**
 * Created by shuangbofu on 2022/3/30 00:46
 */
public enum DatasourceType {
    MySQL, HIVE,
    ;

    public boolean supportJdbc() {
        return Lists.newArrayList(MySQL, HIVE).contains(this);
    }

}
