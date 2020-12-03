package com.github.lucbui.fracktail3.spring.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Repeatable(OnExceptionRespond.Wrapper.class)
public @interface OnExceptionRespond {
    FString value();
    RespondType respondType() default RespondType.INLINE;
    Class<? extends Throwable>[] exception() default Throwable.class;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface Wrapper {
        OnExceptionRespond[] value();
    }
}
