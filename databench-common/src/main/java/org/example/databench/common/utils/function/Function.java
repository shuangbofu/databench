package org.example.databench.common.utils.function;

@FunctionalInterface
public interface Function<T, R, E extends Throwable> {
    R apply(T t) throws E;
}


