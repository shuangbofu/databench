package org.example.databench.service.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2021/9/11 3:29 下午
 */
public abstract class AbstractService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    private void copyProperties(Object source, Object target, String... ignoredProperties) {
        BeanUtils.copyProperties(source, target, ignoredProperties);
    }

    @SafeVarargs
    protected final <T> void arrayForeach(Consumer<T> consumer, T... ts) {
        Optional.ofNullable(ts)
                .filter(i -> i.length > 0)
                .map(Arrays::stream)
                .ifPresent(i -> i.forEach(consumer));
    }

    @SafeVarargs
    protected final <A, B> B aToB(A a, Class<B> bClass, BiConsumer<A, B>... customMappers) {
        B b = null;
        if (a == null) {
            return b;
        }
        if (a.getClass() == bClass) {
            return (B) a;
        }
        try {
            b = bClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error("New instance error", e);
        }
        return copyAToB(a, b, customMappers);
    }

    @SafeVarargs
    protected final <A, B> B copyAToB(A a, B b, BiConsumer<A, B>... customMappers) {
        if (a != null && b != null) {
            copyProperties(a, b);
            arrayForeach(consumer -> consumer.accept(a, b), customMappers);
            return b;
        }
        return b;
    }

    @SafeVarargs
    protected final <A, B> B copyAToBWithoutId(A a, B b, BiConsumer<A, B>... customMappers) {
        if (a != null && b != null) {
            copyProperties(a, b, "id");
            arrayForeach(consumer -> consumer.accept(a, b), customMappers);
            return b;
        }
        return b;
    }

    @SafeVarargs
    protected final <A, B> List<B> listAToListB(List<A> aList, Class<B> bClass, BiConsumer<A, B>... customMappers) {
        return aList.stream().map(i -> aToB(i, bClass, customMappers)).collect(Collectors.toList());
    }
}
