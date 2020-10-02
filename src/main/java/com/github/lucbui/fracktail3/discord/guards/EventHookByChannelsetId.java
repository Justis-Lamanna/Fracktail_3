package com.github.lucbui.fracktail3.discord.guards;

import com.github.lucbui.fracktail3.discord.hook.DiscordEventContext;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public class EventHookByChannelsetId implements DiscordEventHookGuard<Event>{
    private final String id;
    private final boolean _default;

    public EventHookByChannelsetId(String id, boolean aDefault) {
        this.id = id;
        _default = aDefault;
    }

    public EventHookByChannelsetId(String id) {
        this(id, false);
    }

    @Override
    public Mono<Boolean> matches(Bot bot, DiscordEventContext ctx, Event event) {
        return Mono.justOrEmpty(ctx.getConfig().getChannelset(id))
                .flatMap(du -> du.matchesForEvent(event))
                .defaultIfEmpty(_default);
    }
}
