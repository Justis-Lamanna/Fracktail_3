package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.filter.CommandFilter;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Mono;

import java.util.List;

public class Command {
    private final String id;
    private final Resolver<List<String>> names;
    private final CommandFilter commandFilter;
    private final ActionOptions actions;

    public Command(String id, Resolver<List<String>> names, CommandFilter commandFilter, ActionOptions actions) {
        this.id = id;
        this.names = names;
        this.commandFilter = commandFilter;
        this.actions = actions;
    }

    public String getId() {
        return id;
    }

    public Resolver<List<String>> getNames() {
        return names;
    }

    public CommandFilter getCommandFilter() {
        return commandFilter;
    }

    public ActionOptions getActions() {
        return actions;
    }

    public Mono<Boolean> matchesTrigger(Bot bot, DiscordContext ctx) {
        return Mono.just(true);
    }

    public Mono<Void> doAction(Bot bot, DiscordContext ctx) {
        return Mono.empty();
    }
}
