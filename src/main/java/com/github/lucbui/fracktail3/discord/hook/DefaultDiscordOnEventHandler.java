package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.event.DiscordHookEvent;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.utils.model.IdStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Default handler which executes all sub-handlers
 */
public class DefaultDiscordOnEventHandler extends IdStore<DiscordEventHook> implements DiscordOnEventHandler {
    /**
     * Initialize this handler
     * @param handlers The sub-handlers to use
     */
    public DefaultDiscordOnEventHandler(List<DiscordEventHook> handlers) {
        super(handlers);
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, DiscordHookEvent<?> event) {
        DiscordEventContext ctx = new DiscordEventContext(configuration, event);
        return Flux.fromIterable(getAll())
                .filter(h -> h.getSupportedEvents().contains(event.getEventType()))
                .filterWhen(h -> h.matches(bot, ctx))
                .flatMap(h -> h.handleEvent(bot, ctx))
                .then();
    }
}
