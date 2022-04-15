package org.example.executor.base.service.db;

import java.util.Optional;

/**
 * Created by shuangbofu on 2022/4/15 22:22
 */
public interface Db {
    <T> Optional<T> get(String key, Class<T> tClass);

    void set(String key, Object value);
}
