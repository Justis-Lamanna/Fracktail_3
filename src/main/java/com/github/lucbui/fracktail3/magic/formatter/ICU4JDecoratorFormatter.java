package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

public class ICU4JDecoratorFormatter implements ContextFormatter {
    private final ContextFormatter toWrap;

    public ICU4JDecoratorFormatter(ContextFormatter toWrap) {
        this.toWrap = toWrap;
    }

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
