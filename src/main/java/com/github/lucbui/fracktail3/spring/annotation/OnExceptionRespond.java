package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.spring.annotation.strategy.OnExceptionRespondStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.ExceptionStrategy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Repeatable(OnExceptionRespond.Wrapper.class)
@ExceptionStrategy(OnExceptionRespondStrategy.class)
public @interface OnExceptionRespond {
    FString value();
    Class<? extends Throwable>[] exception() default Throwable.class;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface Wrapper {
        OnExceptionRespond[] value();
    }
}
