package com.github.lucbui.fracktail3.spring.util;

import java.util.*;
import java.util.function.Supplier;

public class Defaults {
    private static final Map<Class<?>, Supplier<?>> suppliers = new HashMap<>();

    static {
        suppliers.put(Integer.TYPE, () -> 0);
        suppliers.put(Long.TYPE, () -> 0L);
        suppliers.put(Double.TYPE, () -> 0.0);
        suppliers.put(Optional.class, Optional::empty);
        suppliers.put(OptionalInt.class, OptionalInt::empty);
        suppliers.put(OptionalLong.class, OptionalLong::empty);
        suppliers.put(OptionalDouble.class, OptionalDouble::empty);
    }

    public static <T> T getDefault(Class<T> clazz) {
        if(suppliers.containsKey(clazz)) {
            return clazz.cast(suppliers.get(clazz).get());
        }
        return null;
    }
}
