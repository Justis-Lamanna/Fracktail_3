package com.github.milomarten.fracktail3.spring.command.handler;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

import java.util.Set;

/**
 * Allows access from a set to be simpler to implement from a roles standpoint
 * Attempting to read a property from a set via this accessor instead acts as a check
 * for that property in the set. For instance, roles.admin will return true or false, depending
 * on if the admin property is in the roles set.
 */
public enum SetPropertyAccessor implements PropertyAccessor {
    INSTANCE;

    private static TypeDescriptor BOOLEAN_TYPE = TypeDescriptor.valueOf(Boolean.TYPE);

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class<?>[]{Set.class};
    }

    @Override
    public boolean canRead(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        return o instanceof Set;
    }

    @Override
    public TypedValue read(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        if(o == null) {
            return TypedValue.NULL;
        }
        Set<?> set = (Set<?>) o;
        return new TypedValue(set.contains(s), BOOLEAN_TYPE);
    }

    @Override
    public boolean canWrite(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        return false;
    }

    @Override
    public void write(EvaluationContext evaluationContext, Object o, String s, Object o1) throws AccessException {
        throw new AccessException("Cannot write to Set like that");
    }
}
