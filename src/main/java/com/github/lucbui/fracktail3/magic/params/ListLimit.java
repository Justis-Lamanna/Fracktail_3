package com.github.lucbui.fracktail3.magic.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Arrays;
import java.util.Collection;

/**
 * A Limit which indicates a list of objects is expected
 */
@Getter
public class ListLimit extends ClassLimit {
    private final TypeLimits innerLimit;

    /**
     * Create this limit
     * @param collectionType The type of the *collection*
     * @param innerLimit TypeLimit to enforce on the members of the collection
     */
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
