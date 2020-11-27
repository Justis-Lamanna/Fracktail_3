package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.core.publisher.Mono;

public class ParameterSizeGuard implements Guard {
    private final int minimum;
    private final int maximum;

    public ParameterSizeGuard(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public Mono<Boolean> matches(PlatformBaseContext<?> ctx) {
        if (ctx instanceof CommandUseContext) {
            CommandUseContext<?> cctx = (CommandUseContext<?>) ctx;
            int size = cctx.getParameters().getNumberOfParameters();
            return Mono.just(size >= minimum && size <= maximum);
        }
        return Mono.just(true);
    }
}
