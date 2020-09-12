package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

public abstract class SendMessageAction implements Action {
    private final Resolver<String> resolver;

    public SendMessageAction(Resolver<String> resolver) {
        this.resolver = resolver;
    }

    public Resolver<String> getResolver() {
        return resolver;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        String message = context.resolve(resolver);
        MessageFormat format = new MessageFormat(message, context.getLocale());
        return context.getExtendedVariableMap()
                .map(format::format)
                .flatMap(str -> sendMessage(context, str))
                .then();
    }

    protected abstract Mono<?> sendMessage(CommandContext context, String message);
}
