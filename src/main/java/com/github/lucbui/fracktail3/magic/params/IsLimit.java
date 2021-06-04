package com.github.lucbui.fracktail3.magic.params;

import org.springframework.core.convert.TypeDescriptor;

import java.util.Objects;

public class IsLimit<T> extends ClassLimit {
    private final T obj;

    public IsLimit(T obj) {
        super(TypeDescriptor.forObject(obj));
        this.obj = obj;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && Objects.equals(this.obj, obj);
    }
}
