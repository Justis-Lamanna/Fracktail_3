package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

public class AlertAction implements Action {
    private final Resolver<String> resolver;

    public AlertAction(Resolver<String> resolver) {
        this.resolver = resolver;
    }

    public Resolver<String> getResolver() {
        return resolver;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext ctx) {
        String message = resolver.resolve(ctx.getConfiguration(), ctx.getLocale());
        MessageFormat format = new MessageFormat(message, ctx.getLocale());
        return ctx.getExtendedVariableMap()
                .map(format::format)
                .flatMap(ctx::alert)
                .then();
    }
}
