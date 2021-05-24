package com.github.lucbui.fracktail3.spring.command.handler;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import reactor.core.publisher.Mono;

/**
 * Allows access to the value of the Mono via an async property
 * Using mono.async will wait for the mono to resolve, and then proceed
 * down the tree. This functions identically to a block() command.
 */
public enum MonoPropertyAccessor implements PropertyAccessor {
    INSTANCE;

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[]{Mono.class};
    }

    @Override
    public boolean canRead(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        return o instanceof Mono && s.equals("async");
    }

    @Override
    public TypedValue read(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        if(o == null) {
            return TypedValue.NULL;
        }

        Mono<?> readObj = (Mono<?>) o;
        Object resolvedObj = readObj.block();
        if (resolvedObj == null) {
            return TypedValue.NULL;
        } else {
            return new TypedValue(resolvedObj);
        }
    }

    @Override
    public boolean canWrite(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        return false;
    }

    @Override
    public void write(EvaluationContext evaluationContext, Object o, String s, Object o1) throws AccessException {
        throw new AccessException("Cannot write to Mono");
    }
}
