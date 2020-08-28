package com.github.lucbui.fracktail3.magic.handlers.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public interface DiscordHandler {
    Mono<Void> execute(Bot bot, DiscordConfiguration configuration, MessageCreateEvent event);
}
