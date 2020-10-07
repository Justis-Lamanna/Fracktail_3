package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

/**
 * Class which encapsulates a formattable string
 */
public class FormattedString {
    private static ContextFormatter _default = new ICU4JDecoratorFormatter();

    private final String raw;
    private final ContextFormatter formatter;

    private FormattedString(String raw, ContextFormatter formatter) {
        this.raw = raw;
        this.formatter = formatter;
    }

    /**
     * Create an unformatted format string
     * @param str The string to return
     * @return The created FormattedString
     */
    public static FormattedString literal(String str) {
        return from(str, ContextFormatter.identity());
    }

    /**
     * Create an format string using the default formatter
     * @param str The string to return
     * @return The created FormattedString
     */
    public static FormattedString from(String str) {
        return from(str, _default);
    }

    /**
     * Create an format string using the supplied formatter
     * @param str The string to return
     * @param formatter The formatter to use
     * @return The created FormattedString
     */
    public static FormattedString from(String str, ContextFormatter formatter) {
        return new FormattedString(str, formatter);
    }

    /**
     * Get the raw text string
     * @return The raw string
     */
    public String getRaw() {
        return raw;
    }

    /**
     * Get the formatter being used
     * @return The formatter used
     */
    public ContextFormatter getFormatter() {
        return formatter;
    }

    /**
     * Get the formatted string
     * @param ctx The context of the string
     * @return The formatted value
     */
    public Mono<String> getFor(CommandContext ctx) {
        return formatter.format(raw, ctx);
    }

    /**
     * Set the default formatter to use
     * @param formatter The new default formatter
     */
    public static void setDefaultFormatter(ContextFormatter formatter) {
        FormattedString._default = formatter;
    }

    /**
     * Get the default formatter
     * @return The default formatter
     */
    public static ContextFormatter getDefaultFormatter() {
        return _default;
    }
}