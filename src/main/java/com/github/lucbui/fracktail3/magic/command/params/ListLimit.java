package com.github.lucbui.fracktail3.magic.command.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Arrays;
import java.util.Collection;

@Getter
public class ListLimit extends ClassLimit {
    private final TypeLimits innerLimit;

    public ListLimit(TypeDescriptor collectionType, TypeLimits innerLimit) {
        super(collectionType);
        this.innerLimit = innerLimit;
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && innerMatches(obj);
    }

    private boolean innerMatches(Object obj) {
        if(getTypeDescriptor().isArray()) {
            return Arrays.stream((Object[])obj).allMatch(innerLimit::matches);
        } else if(getTypeDescriptor().isCollection()) {
            Collection<?> collection = (Collection<?>) obj;
            return collection.stream().allMatch(innerLimit::matches);
        } else {
            return false;
        }
    }
}
