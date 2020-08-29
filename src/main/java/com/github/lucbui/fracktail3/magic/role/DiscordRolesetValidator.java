package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import reactor.core.publisher.Mono;

public interface DiscordRolesetValidator {
    Mono<Boolean> validateInDiscordRole(BotSpec botSpec, DiscordContext ctx);

    static DiscordRolesetValidator identity(boolean value) {
        return (b, c) -> Mono.just(value);
    }
}
