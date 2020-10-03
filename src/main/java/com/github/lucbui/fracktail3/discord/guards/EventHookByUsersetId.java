package com.github.lucbui.fracktail3.discord.guards;

import com.github.lucbui.fracktail3.discord.hook.DiscordEventContext;
import com.github.lucbui.fracktail3.magic.Bot;
import reactor.core.publisher.Mono;

public class EventHookByUsersetId implements DiscordEventHookGuard{
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
    public Mono<Boolean> matches(Bot bot, DiscordEventContext ctx) {
        return Mono.justOrEmpty(ctx.getConfig().getUserset(id))
                .flatMap(du -> du.matchesForEvent(ctx.getEvent()))
                .defaultIfEmpty(_default);
    }
}
