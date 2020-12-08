package com.github.lucbui.fracktail3.spring.util;

import java.util.*;
import java.util.function.Supplier;

/**
 * Utility class providing default values for various types.
 * Defaults are:
 * - int, Integer: 0
 * - long, Long: 0
 * - double, Double: 0.0
 * - Optional: Optional.empty()
 * - OptionalInt: OptionalInt.empty()
 * - OptionalLong: OptionalLong.empty()
 * - OptionalDouble: OptionalDouble.empty()
 * All other types are null-defaults
 */
public class Defaults {
    private static final Map<Class<?>, Supplier<?>> suppliers = new HashMap<>();

    static {
        suppliers.put(Integer.TYPE, () -> 0);
        suppliers.put(Long.TYPE, () -> 0L);
        suppliers.put(Double.TYPE, () -> 0.0);
        suppliers.put(Integer.class, () -> 0);
        suppliers.put(Long.class, () -> 0L);
        suppliers.put(Double.class, () -> 0.0);
        suppliers.put(Optional.class, Optional::empty);
        suppliers.put(OptionalInt.class, OptionalInt::empty);
        suppliers.put(OptionalLong.class, OptionalLong::empty);
        suppliers.put(OptionalDouble.class, OptionalDouble::empty);
    }

    /**
     * Return the default for the given class
     * @param clazz The class to get
     * @param <T> The type to retrieve
     * @return The default value, or null if there is none
     */
    public static <T> T getDefault(Class<T> clazz) {
        if(suppliers.containsKey(clazz)) {
            return (T) suppliers.get(clazz).get();
        }
        return null;
    }
}
