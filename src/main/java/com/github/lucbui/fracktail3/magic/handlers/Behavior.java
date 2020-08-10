package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Behavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(Behavior.class);

    private final int paramCount;
    private final NamedParametersConfiguration namedParameters;
    private final String role;
    private final Action action;

    public Behavior(int paramCount, NamedParametersConfiguration namedParameters, String role, Action action) {
        this.paramCount = paramCount;
        this.namedParameters = namedParameters;
        this.role = role;
        this.action = action;
    }

    public boolean matches(Bot bot, CommandContext context) {
        return this.paramCount == -1 || context.getNormalizedParameters().length == this.paramCount;
    }

    public Mono<Void> doAction(Bot bot, CommandContext context){
        LOGGER.info("Performing action: {}", action);
        return action.doAction(bot, context, namedParameters.resolve(context));
    }
}
