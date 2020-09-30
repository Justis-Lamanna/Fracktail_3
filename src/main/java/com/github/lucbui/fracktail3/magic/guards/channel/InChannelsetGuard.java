package com.github.lucbui.fracktail3.magic.guards.channel;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.guards.Guard;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

public class InChannelsetGuard implements Guard {
    private final String id;
    private final boolean defaultValue;

    public InChannelsetGuard(String id) {
        this.id = id;
        this.defaultValue = true;
    }

    public InChannelsetGuard(String id, boolean defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext ctx) {
        return bot.getSpec().getChannelset(id)
                .map(cs -> cs.matches(bot, ctx))
                .orElse(Mono.just(defaultValue));
    }
}
