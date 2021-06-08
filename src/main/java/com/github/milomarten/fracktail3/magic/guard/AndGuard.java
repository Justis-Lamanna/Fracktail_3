package com.github.milomarten.fracktail3.magic.guard;

import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
public class AndGuard implements Guard {
    private final Guard[] guards;

    @Override
    public Mono<Boolean> matches(CommandUseContext ctx) {
        return Flux.fromArray(guards)
                .flatMap(g -> g.matches(ctx))
                .all(b -> b);
    }
}
