package com.github.lucbui.fracktail3.magic.guard;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

@Data
public class NotGuard implements Guard {
    private final Guard base;

    @Override
    public Mono<Boolean> matches(CommandUseContext ctx) {
        return BooleanUtils.not(base.matches(ctx));
    }
}
