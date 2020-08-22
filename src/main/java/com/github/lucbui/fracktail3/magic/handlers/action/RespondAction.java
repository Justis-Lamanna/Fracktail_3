package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
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
        String message = resolver.resolve(bot.getDiscordConfiguration().orElseThrow(CommandUseException::new), ctx.getLocale());
        return ctx.getMessage().getMessage()
                .getChannel()
                .flatMap(c -> c.createMessage(message))
                .then();
    }
}
