package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

public class RespondAction extends AbstractAction {
    private final Resolver<String> resolver;

    public RespondAction(Resolver<String> resolver) {
        this.resolver = resolver;
    }

    public Resolver<String> getResolver() {
        return resolver;
    }

    @Override
    public Mono<Void> doDiscordAction(Bot bot, DiscordContext ctx) {
        String message = resolver.resolve(ctx.getConfiguration(), ctx.getLocale());
        MessageFormat format = new MessageFormat(message, ctx.getLocale());
        if(DiscordContext.containsExtendedVariable(message)) {
            return ctx.getExtendedVariableMap()
                    .map(format::format)
                    .flatMap(ctx::respond)
                    .then();
        } else {
            return ctx.respond(format.format(ctx.getVariableMap())).then();
        }
    }
}
