package org.example.databench.common.enums;

/**
 * Created by shuangbofu on 2021/9/10 9:42 下午
 */
public enum ModuleType {
    development, query, api, resource, document;

    public boolean isDevelop() {
        return development.equals(this);
    }
}
