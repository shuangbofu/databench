package org.example.databench.common.utils.function;

/**
 * Created by shuangbofu on 2021/8/18 10:44 下午
 */
public interface BiFunction<T, U, R, E extends Throwable> {
    R apply(T t, U u) throws E, Exception;
}
