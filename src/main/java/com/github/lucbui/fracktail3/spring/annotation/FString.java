package com.github.lucbui.fracktail3.spring.annotation;

public @interface FString {
    String value();
    Formatter[] formatters() default {};
}
