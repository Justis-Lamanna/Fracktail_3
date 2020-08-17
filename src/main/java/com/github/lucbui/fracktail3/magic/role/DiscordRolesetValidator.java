package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import reactor.core.publisher.Mono;

public interface DiscordRolesetValidator {
    Mono<Boolean> validateInDiscordRole(Bot bot, DiscordContext ctx);

    static DiscordRolesetValidator identity(boolean value) {
        return (b, c) -> Mono.just(value);
    }
}
