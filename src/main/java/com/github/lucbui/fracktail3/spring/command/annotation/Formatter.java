package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;

/**
 * Annotation-friendly way to construct a ContextFormatter
 */
public @interface Formatter {
    /**
     * The class of the ContextFormatter to use
     * @return the class of the ContextFormatter to use
     */
    Class<? extends ContextFormatter> value();

    /**
     * One or more string parameters to inject in the constructor
     * @return String parameters to inject in the constructor
     */
    String[] params() default {};
}
