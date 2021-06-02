package com.github.lucbui.fracktail3.magic.guard;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class IdentityGuard implements Guard {
    private final boolean value;

    @Override
    public Mono<Boolean> matches(CommandUseContext ctx) {
        return Mono.just(value);
    }
}
