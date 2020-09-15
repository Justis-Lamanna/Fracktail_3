package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.filterset.FilterSetValidator;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class UsersetById implements FilterSetValidator {
    private final String id;

    public UsersetById(String id) {
        this.id = id;
    }

    public Optional<Userset> retrieve(BotSpec spec) {
        return spec.getUserset(id);
    }

    @Override
    public Mono<Boolean> validate(BotSpec botSpec, CommandContext ctx) {
        return Mono.justOrEmpty(retrieve(botSpec))
                    .flatMap(u -> u.validate(botSpec, ctx))
                    .defaultIfEmpty(true);
    }
}
