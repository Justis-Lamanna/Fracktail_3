package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.filter.CommandFilter;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class Command {
    private final String id;
    private final Resolver<List<String>> names;
    private final CommandFilter commandFilter;
    private final ActionOptions actions;

    public Command(String id, Resolver<List<String>> names, CommandFilter commandFilter, ActionOptions actions) {
        this.id = Objects.requireNonNull(id);
        this.names = Objects.requireNonNull(names);
        this.commandFilter = Objects.requireNonNull(commandFilter);
        this.actions = Objects.requireNonNull(actions);
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

    public Mono<Boolean> matchesTrigger(Bot bot, CommandContext<?> ctx) {
        return commandFilter.matches(bot, ctx);
    }

    public Mono<Void> doAction(Bot bot, CommandContext<?> ctx) {
        return actions.doAction(bot, ctx);
    }
}
