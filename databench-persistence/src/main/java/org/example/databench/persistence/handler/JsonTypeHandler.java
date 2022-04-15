package org.example.databench.persistence.handler;

import io.github.shuangbofu.helper.mybatis.handler.BaseJsonTypeHandler;
import org.example.databench.lib.utils.JSONUtils;

public abstract class JsonTypeHandler<T> extends BaseJsonTypeHandler<T> {

    private final Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T parseToObj(String s) {
        return JSONUtils.parseObject(s, clazz);
    }

    @Override

    public String toJsonString(T t) {
        return JSONUtils.toJSONString(t);
    }
}
