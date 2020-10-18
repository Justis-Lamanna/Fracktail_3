package com.github.lucbui.fracktail3.magic.guards.channel;

import com.github.lucbui.fracktail3.magic.guards.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
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
    public Mono<Boolean> matches(BaseContext<?> ctx) {
        return ctx.getPlatform().getConfig().getChannelset(id)
                .map(cs -> cs.matches(ctx))
                .orElse(Mono.just(defaultValue));
    }
}
