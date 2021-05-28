package com.github.lucbui.fracktail3.magic.command.params;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.TypeDescriptor;

@Getter
public class RangeLimit<T extends Comparable<T>> extends ClassLimit {
    private final T lower;
    private final T upper;

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
