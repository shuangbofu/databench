package org.example.databench.common.utils.function;

/**
 * Created by shuangbofu on 2021/12/7 16:02
 */
public interface BiConsumer<T, R, E extends Throwable> {
    void accept(T t, R r) throws E;
}
