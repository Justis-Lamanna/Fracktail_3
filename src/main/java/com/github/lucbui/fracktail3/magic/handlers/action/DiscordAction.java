package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import reactor.core.publisher.Mono;

public interface DiscordAction {
    Mono<Void> discordAction(Bot bot, DiscordConfiguration configuration, DiscordContext discordContext);
}
