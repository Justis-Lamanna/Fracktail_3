package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Decorator formatter which uses the ICU4J MessageFormat functionality to format a string
 * This allows placeholders to be resolved into concrete values, depending on what the context provides.
 */
public class ICU4JDecoratorFormatter implements ContextFormatter {
    @Override
    public Mono<String> format(String raw, BaseContext<?> ctx, Map<String, Object> addlVariables) {
        Map<String, Object> map = new AsynchronousMap<>(ctx.getMap());
        map.putAll(addlVariables);
        return ctx.getLocale()
                .map(locale -> new MessageFormat(raw, locale))
                .map(format -> format.format(map));
    }
}
