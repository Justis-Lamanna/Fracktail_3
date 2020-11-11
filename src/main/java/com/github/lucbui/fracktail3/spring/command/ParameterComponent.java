package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ParameterComponent {
    Function<CommandUseContext<?>, Object> func;
    Function<PlatformBaseContext<?>, Mono<Boolean>> guard;

    public ParameterComponent(Function<CommandUseContext<?>, Object> func) {
        this.func = func;
    }

    public ParameterComponent(Function<CommandUseContext<?>, Object> func, Function<PlatformBaseContext<?>, Mono<Boolean>> guard) {
        this.func = func;
        this.guard = guard;
    }
}
