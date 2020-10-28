package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

/**
 * Decorator formatter which uses the ICU4J MessageFormat functionality to format a string
 * This allows placeholders to be resolved into concrete values, depending on what the context provides.
 */
public class ICU4JDecoratorFormatter implements ContextFormatter {

    @Override
    public Mono<String> format(String raw, BaseContext<?> ctx) {
        return ctx.getLocale()
                .map(locale -> new MessageFormat(raw, locale))
                .map(format -> format.format(ctx.getMap()));
    }
}
