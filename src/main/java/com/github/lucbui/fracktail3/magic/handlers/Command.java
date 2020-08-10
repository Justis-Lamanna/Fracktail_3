package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

public class Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(Command.class);

    private final Resolver<String> name;
    private final Resolver<List<String>> aliases;
    private final String role;
    private final List<Behavior> behaviors;
    private final Action orElse;

    public Command(Resolver<String> name, Resolver<List<String>> aliases, String role, List<Behavior> behaviors, Action orElse) {
        this.name = name;
        this.aliases = aliases;
        this.role = role;
        this.behaviors = behaviors;
        this.orElse = orElse;
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

    public Action getOrElse() {
        return orElse;
    }

    public Mono<Void> doAction(Bot bot, CommandContext context) {
        for(Behavior b : behaviors) {
            if(b.matches(bot, context)) {
                return b.doAction(bot, context);
            }
        }
        LOGGER.debug("Executing unknown behavior for command {} | Contents: {}", context.getCommand(), context.getContents());
        if(orElse != null) {
            return orElse.doAction(bot, context, NamedParameters.EMPTY);
        }
        return Mono.empty();
    }
}
