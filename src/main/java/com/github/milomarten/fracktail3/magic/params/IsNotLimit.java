package com.github.milomarten.fracktail3.magic.params;

import org.springframework.core.convert.TypeDescriptor;

import java.util.Objects;

public class IsNotLimit<T> extends ClassLimit {
    private final T obj;

    public IsNotLimit(T obj) {
        super(TypeDescriptor.forObject(obj));
        this.obj = obj;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && !Objects.equals(this.obj, obj);
    }
}
