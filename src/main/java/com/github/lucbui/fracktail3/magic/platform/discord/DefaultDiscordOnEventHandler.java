package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Default handler which executes all sub-handlers
 */
public class DefaultDiscordOnEventHandler implements DiscordOnEventHandler {
    private final DiscordPlatform platform;
    private final List<DiscordEventHandler> handlers;

    /**
     * Initialize this handler
     * @param platform The platform of the handler
     * @param handlers The sub-handlers to use
     */
    public DefaultDiscordOnEventHandler(DiscordPlatform platform, List<DiscordEventHandler> handlers) {
        this.platform = platform;
        this.handlers = handlers;
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, Event event) {
        DiscordEventContext ctx = new DiscordEventContext(platform, configuration, event);
        return Flux.fromIterable(handlers)
                .filter(h -> h.canHandleEvent(event))
                .flatMap(h -> h.handleEvent(bot, ctx))
                .then();
    }
}
