package com.github.lucbui.fracktail3.magic.filterset.channel;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

public class InChannelsetFilter implements Filter {
    private final String id;
    private final boolean defaultValue;

    public InChannelsetFilter(String id) {
        this.id = id;
        this.defaultValue = true;
    }

    public InChannelsetFilter(String id, boolean defaultValue) {
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
