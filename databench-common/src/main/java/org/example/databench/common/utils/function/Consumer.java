package org.example.databench.common.utils.function;

/**
 * Created by shuangbofu on 2021/8/18 16:48
 */
@FunctionalInterface
public interface Consumer<T, V extends Throwable> {
    void accept(T t) throws V;
}
