package com.github.lucbui.fracktail3.magic.params;

import lombok.Getter;
import org.apache.commons.collections4.ComparatorUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Comparator;
import java.util.List;

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
    private RangeLimit(TypeDescriptor type, @Nullable T lower, @Nullable T upper) {
        super(type);
        this.lower = lower;
        this.upper = upper;
    }

    public static <T extends Comparable<T>> RangeLimit<T> max(T upper) {
        return new RangeLimit<>(TypeDescriptor.forObject(upper), null, upper);
    }

    public static <T extends Comparable<T>> RangeLimit<T> min(T lower) {
        return new RangeLimit<>(TypeDescriptor.forObject(lower), lower, null);
    }

    public static <T extends Comparable<T>> RangeLimit<T> between(T lower, T upper) {
        return new RangeLimit<>(TypeDescriptor.forObject(lower), lower, upper);
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) &&
                (lower == null || ((T)obj).compareTo(lower) >= 0) &&
                (upper == null || ((T)obj).compareTo(upper) <= 0);
    }

    @Override
    public Object getDefault() {
        return isOptional() ? super.getDefault() : lower;
    }

    public static <T extends Comparable<T>> RangeLimit<T> merge(List<RangeLimit<T>> ranges) {
        T max = null;
        T min = null;
        Comparator<T> c = Comparator.naturalOrder();
        for(RangeLimit<T> range : ranges) {
            max = (max == null) ? range.getUpper() : ComparatorUtils.max(max, range.getUpper(), c);
            min = (min == null) ? range.getUpper() : ComparatorUtils.min(min, range.getLower(), c);
        }
        TypeDescriptor td = ranges.isEmpty() ? TypeDescriptor.valueOf(Object.class) : ranges.get(0).getTypeDescriptor();
        return new RangeLimit<>(td, min, max);
    }
}