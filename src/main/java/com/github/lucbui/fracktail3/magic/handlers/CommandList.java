package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class CommandList {
    private final List<Command> commands;
    private final Action orElse;

    public CommandList(List<Command> commands, Action orElse) {
        this.commands = Collections.unmodifiableList(commands);
        this.orElse = orElse;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Action getOrElse() {
        return orElse;
    }

    public Mono<Void> doOrElse(Bot bot, CommandContext ctx) {
        if(orElse == null) {
            return Mono.empty();
        }
        return orElse.doAction(bot, ctx);
    }
}
