package com.github.lucbui.fracktail3.magic.command;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Comparator;

@Getter
public class RangeLimit<T extends Comparable<T>> extends ClassLimit {
    private Comparator<T> comparator;
    private final T lower;
    private final T upper;

    public RangeLimit(TypeDescriptor type, T lower, T upper) {
        super(type);
        this.comparator = Comparator.naturalOrder();
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) &&
                comparator.compare((T)obj, lower) >= 0 &&
                comparator.compare((T)obj, upper) <= 0;
    }
}
