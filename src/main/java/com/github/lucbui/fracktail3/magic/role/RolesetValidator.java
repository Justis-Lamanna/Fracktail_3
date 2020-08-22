package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public interface RolesetValidator {
    Mono<Boolean> validateInRole(Bot bot, CommandContext ctx);
}
