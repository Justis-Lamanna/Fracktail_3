package com.github.lucbui.fracktail3.magic.formatter;

import java.util.Objects;

public class ContextFormatters {
    private static ContextFormatter _default = new ICU4JDecoratorFormatter(new TranslatorFormatter());

    public static ContextFormatter getDefault() {
        return _default;
    }

    public static void setDefault(ContextFormatter newDefault) {
        ContextFormatters._default = Objects.requireNonNull(newDefault);
    }
}
