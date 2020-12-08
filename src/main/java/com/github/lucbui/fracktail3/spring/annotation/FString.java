package com.github.lucbui.fracktail3.spring.annotation;

/**
 * Annotation-friendly way to specify a FormattedString
 */
public @interface FString {
    /**
     * The raw value of the FormattedString
     * @return The raw value of the FormattedString
     */
    String value();

    /**
     * One or more Formatters to use to transform the raw value
     * @return Formatters to use to transform the raw value
     */
    Formatter[] formatters() default {};
}
