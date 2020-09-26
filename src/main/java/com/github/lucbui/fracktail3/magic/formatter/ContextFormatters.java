package com.github.lucbui.fracktail3.magic.formatter;

import java.util.Objects;

/**
 * For now, solely maintains a default ContextFormatter
 * By default, the default formatter an ICU4JDecoratorFormatter, with no intermediate formatting.
 * You can change this formatter to use a translator, if translation becomes the default for your bot.
 */
public class ContextFormatters {
    private static final ContextFormatter DEFAULT = new ICU4JDecoratorFormatter();
    private static ContextFormatter _default = DEFAULT;

    /**
     * Get the default ContextFormatter.
     * @return The default ContextFormatter.
     */
    public static ContextFormatter getDefault() {
        return _default;
    }

    /**
     * Set the default ContextFormatter.
     * @param newDefault The new default ContextFormatter
     */
    public static void setDefault(ContextFormatter newDefault) {
        ContextFormatters._default = Objects.requireNonNull(newDefault);
    }

    /**
     * Reset to the original default ContextFormatter
     */
    public static void resetDefault() {
        setDefault(DEFAULT);
    }
}
