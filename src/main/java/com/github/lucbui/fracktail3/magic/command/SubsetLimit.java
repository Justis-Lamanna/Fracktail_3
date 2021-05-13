package com.github.lucbui.fracktail3.magic.command;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SubsetLimit<T> extends ClassLimit {
    private List<T> acceptableValues;

    public SubsetLimit(TypeDescriptor type, List<T> acceptableValues) {
        super(type);
        this.acceptableValues = new ArrayList<>(acceptableValues);
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && acceptableValues.contains(obj);
    }
}
