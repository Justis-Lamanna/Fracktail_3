package com.github.lucbui.fracktail3.magic.params;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Allows for further limiting of only values within a range
 * @param <T> The type being considered
 */
@Getter
public class RangeLimit<T extends Comparable<T>> extends ClassLimit {
    private final T lower;
    private final T upper;

    /**
     * Create the range limit
     * @param type The type of permissible objects
     * @param lower The lower bound, or null if none
     * @param upper The upper bound, or null if none
     */
    public RangeLimit(TypeDescriptor type, @Nullable T lower, @Nullable T upper) {
        super(type);
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) &&
                (lower == null || ((T)obj).compareTo(lower) >= 0) &&
                (upper == null || ((T)obj).compareTo(upper) <= 0);
    }
}
