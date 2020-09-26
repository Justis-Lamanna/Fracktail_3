package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

/**
 * Decorator formatter which uses the ICU4J MessageFormat functionality to format a string
 * This allows placeholders to be resolved into concrete values, depending on what the context provides.
 */
public class ICU4JDecoratorFormatter implements ContextFormatter {
    private final ContextFormatter toWrap;

    /**
     * Initialize this formatter with a nested ContextFormatter
     * This allows the input string to be processed in some way before placeholder resolution
     * @param toWrap The wrapped formatter
     */
    public ICU4JDecoratorFormatter(ContextFormatter toWrap) {
        this.toWrap = toWrap;
    }

    /**
     * Initialize this former with no nested ContextFormatter
     * Input is formatted directly, without any intermediate steps.
     */
    public ICU4JDecoratorFormatter() {
        this.toWrap = ContextFormatter.identity();
    }

    @Override
    public Mono<String> format(String raw, CommandContext ctx) {
        return toWrap.format(raw, ctx)
                .zipWith(ctx.getExtendedVariableMap())
                .map(t -> {
                    MessageFormat formatting = new MessageFormat(t.getT1(), ctx.getLocale());
                    return formatting.format(t.getT2());
                });
    }
}
