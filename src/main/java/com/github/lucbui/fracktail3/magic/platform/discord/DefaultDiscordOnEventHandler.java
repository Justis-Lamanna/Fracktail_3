package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.utils.model.IdStore;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Default handler which executes all sub-handlers
 */
public class DefaultDiscordOnEventHandler extends IdStore<DiscordEventHandler> implements DiscordOnEventHandler {
    /**
     * Initialize this handler
     * @param handlers The sub-handlers to use
     */
    public DefaultDiscordOnEventHandler(List<DiscordEventHandler> handlers) {
        super(handlers);
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, Event event) {
        DiscordEventContext ctx = new DiscordEventContext(configuration, event);
        return Flux.fromIterable(getAll())
                .filterWhen(h -> h.passesGuard(event))
                .flatMap(h -> h.handleEvent(bot, ctx))
                .then();
    }
}
