package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;

public @interface Formatter {
    Class<? extends ContextFormatter> value();
    String[] params() default {};
}
