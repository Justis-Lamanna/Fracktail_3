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

    @Override
    public Guard and(Guard other) {
        return value ? other : this;
    }

    @Override
    public Guard or(Guard other) {
        return value ? this : other;
    }

    @Override
    public Guard not() {
        return new IdentityGuard(!value);
    }
}
