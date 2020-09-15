package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class UsersetById implements Filter {
    private final String id;

    public UsersetById(String id) {
        this.id = id;
    }

    public Optional<Userset> retrieve(BotSpec spec) {
        return spec.getUserset(id);
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext ctx) {
        return Mono.justOrEmpty(retrieve(bot.getSpec()))
                    .flatMap(u -> u.matches(bot, ctx))
                    .defaultIfEmpty(true);
    }
}
