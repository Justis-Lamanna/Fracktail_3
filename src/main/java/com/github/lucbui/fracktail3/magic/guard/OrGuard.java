package com.github.lucbui.fracktail3.magic.guard;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

@Data
public class OrGuard implements Guard {
    private final Guard one;
    private final Guard two;

    @Override
    public Mono<Boolean> matches(CommandUseContext ctx) {
        return BooleanUtils.or(one.matches(ctx), two.matches(ctx));
    }
}
