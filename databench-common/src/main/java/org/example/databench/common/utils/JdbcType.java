package org.example.databench.common.utils;

import java.util.Optional;

/**
 * Created by shuangbofu on 2021/8/10 17:40
 */
public enum JdbcType {
    mysql("com.mysql.cj.jdbc.Driver"),
    hive("org.apache.hive.jdbc.HiveDriver"),
    hive2("org.apache.hive.jdbc.HiveDriver"),
    presto("io.prestosql.jdbc.PrestoDriver"),
    clickhouse("ru.yandex.clickhouse.ClickHouseDriver"),

    ;

    private final String className;

    JdbcType(String className) {
        this.className = className;
    }

    public static JdbcType parseFromUrl(String url) throws IllegalArgumentException {
        return Optional.ofNullable(url)
                .map(i -> i.trim().split(":"))
                .filter(i -> i.length > 1)
                .map(i -> JdbcType.valueOf(i[1]))
                .orElseThrow(() -> new IllegalArgumentException("Jdbc url illegal"));
    }

    public String getClassName() {
        return className;
    }
}
