package com.github.milomarten.fracktail3.magic.params;

import org.springframework.core.convert.TypeDescriptor;

public abstract class LengthLimit extends ClassLimit {
    private final int minLength;
    private final int maxLength;

    public LengthLimit(Class<?> clazz, int minLength, int maxLength) {
        super(clazz);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public LengthLimit(TypeDescriptor type, int minLength, int maxLength) {
        super(type);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public abstract int length(Object obj);

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) &&
                (minLength == 0 || length(obj) >= minLength) &&
                (maxLength == -1 || length(obj) <= maxLength);
    }
}
