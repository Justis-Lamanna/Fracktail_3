package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
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
        String message = resolver.resolve(bot.getSpec().getDiscordConfiguration().orElseThrow(CommandUseException::new), ctx.getLocale());
        if(DiscordContext.containsExtendedVariable(message)) {
            return ctx.getExtendedVariableMap()
                    .map(mapping -> {
                        MessageFormat format = new MessageFormat(message, ctx.getLocale());
                        return format.format(mapping);
                    })
                    .zipWith(ctx.getMessage().getMessage().getChannel())
                    .flatMap(t -> t.getT2().createMessage(t.getT1()))
                    .then();
        } else {
            MessageFormat format = new MessageFormat(message, ctx.getLocale());
            return ctx.getMessage().getMessage().getChannel()
                    .flatMap(c -> c.createMessage(format.format(ctx.getVariableMap())))
                    .then();
        }
    }
}
