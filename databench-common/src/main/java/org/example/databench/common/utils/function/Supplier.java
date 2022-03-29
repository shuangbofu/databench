package org.example.databench.common.utils.function;

@FunctionalInterface
public interface Supplier<T> {
    T get() throws Exception;
}
