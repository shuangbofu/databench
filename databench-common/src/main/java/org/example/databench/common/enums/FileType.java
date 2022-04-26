package org.example.databench.common.enums;

import lombok.Getter;

/**
 * Created by shuangbofu on 2021/9/11 2:14 下午
 */
@Getter
public enum FileType {

    // 大数据
    shell(FileCategory.bigdata),
    sparkSql(FileCategory.bigdata),
    dataX(FileCategory.bigdata),
    flinkX(FileCategory.bigdata),
    sparkJar(FileCategory.bigdata),
    flinkJar(FileCategory.bigdata),
    file(FileCategory.bigdata),
    jar(FileCategory.bigdata),
    function(FileCategory.bigdata),

    // 数据库
    mysql(FileCategory.database),
    hive(FileCategory.database),

    // 通用
    virtual(FileCategory.common),

    // 数据源
    jdbc_mysql(FileCategory.datasource),
    jdbc_hive(FileCategory.datasource),

    markdown(FileCategory.bigdata),

    // 计算组件
    component_spark(FileCategory.compute),
    component_flink(FileCategory.compute),
    component_hdfs(FileCategory.storage),
    component_yarn(FileCategory.schedule),
    ;

    private final FileCategory category;

    FileType(FileCategory category) {
        this.category = category;
    }

    public boolean isResource() {
        return jar.equals(this) || file.equals(this);
    }

    public boolean haveDag() {
        return !(isResource() || function.equals(this));
    }

    public boolean isJdbcResourceFile() {
        return name().startsWith("jdbc");
    }

    public boolean isDatasource() {
        return category.equals(FileCategory.datasource);
    }

    public DatasourceType toDsType() {
        if (isDatasource()) {
            if (jdbc_mysql.equals(this)) {
                return DatasourceType.MySQL;
            } else if (jdbc_hive.equals(this)) {
                return DatasourceType.HIVE;
            }
        }
        throw new RuntimeException("Not supported");
    }
}
