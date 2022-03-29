package org.example.databench.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by shuangbofu on 2021/8/25 10:30 下午
 */
public class ReflectUtils {
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fieldList;
        for (fieldList = new ArrayList<>(); clazz != null; clazz = clazz.getSuperclass()) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fieldList;
    }

    public static Optional<Field> getField(Object obj, String fieldName) {
        return getFields(obj.getClass()).stream().filter(i -> i.getName().equals(fieldName))
                .findAny().map(i -> {
                    i.setAccessible(true);
                    return i;
                });
    }

    public static Type[] getInterfaceGenericTypes(Class<?> clazz) {
        return ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments();
    }

    public static Type[] getSuperclassGenericTypes(Class<?> clazz) {
        return ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
    }
}
