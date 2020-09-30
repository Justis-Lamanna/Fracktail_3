package com.github.lucbui.fracktail3.discord.guards;

import com.github.lucbui.fracktail3.discord.platform.DiscordEventContext;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public class EventHookByUsersetId implements DiscordEventHookGuard<Event>{
    private final String id;
    private final boolean _default;

    public EventHookByUsersetId(String id, boolean aDefault) {
        this.id = id;
        _default = aDefault;
    }

    public EventHookByUsersetId(String id) {
        this(id, false);
    }

    @Override
    public Mono<Boolean> matches(Bot bot, DiscordEventContext ctx, Event event) {
        return Mono.justOrEmpty(bot.getSpec().getUserset(id))
                .filter(us -> us instanceof DiscordUserset)
                .cast(DiscordUserset.class)
                .flatMap(du -> du.matchesForEvent(event))
                .defaultIfEmpty(_default);
    }
}
