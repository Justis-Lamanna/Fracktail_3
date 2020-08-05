package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Mono;

import java.util.List;

public class Command {
    private final Resolver<String> name;
    private final Resolver<List<String>> aliases;
    private final List<Behavior> behaviors;

    public Command(Resolver<String> name, Resolver<List<String>> aliases, List<Behavior> behaviors) {
        this.name = name;
        this.aliases = aliases;
        this.behaviors = behaviors;
    }

    public Resolver<String> getName() {
        return name;
    }

    public Resolver<List<String>> getAliases() {
        return aliases;
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public Mono<Void> doAction(Bot bot, CommandContext context) {
        for(Behavior b : behaviors) {
            if(b.matches(bot, context)) {
                return b.doAction(bot, context);
            }
        }
        return Mono.empty();
    }
}
