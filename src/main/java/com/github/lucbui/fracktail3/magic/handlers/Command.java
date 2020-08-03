package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Mono;

import java.util.List;

public class Command {
    private final Resolver<String> name;
    private final Resolver<List<String>> aliases;

    public Command(Resolver<String> name, Resolver<List<String>> aliases, Behavior behavior) {
        this.name = name;
        this.aliases = aliases;
    }

    public Resolver<String> getName() {
        return name;
    }

    public Resolver<List<String>> getAliases() {
        return aliases;
    }

    public Mono<Void> doDiscordAction(Bot bot, DiscordConfiguration configuration, DiscordContext discordContext) {
        return Mono.empty();
    }
}
