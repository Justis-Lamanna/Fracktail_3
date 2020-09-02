package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public interface UsersetValidator {
    Mono<Boolean> validateInRole(BotSpec botSpec, CommandContext<?> ctx);
}
