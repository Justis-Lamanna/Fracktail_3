package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import reactor.core.publisher.Mono;

public class Behavior {
    private final int paramCount;
    private final NamedParametersConfiguration namedParameters;
    private final Action action;

    public Behavior(int paramCount, NamedParametersConfiguration namedParameters, Action action) {
        this.paramCount = paramCount;
        this.namedParameters = namedParameters;
        this.action = action;
    }

    public boolean matches(Bot bot, CommandContext context) {
        return this.paramCount == -1 || context.getNormalizedParameters().length == this.paramCount;
    }

    public Mono<Void> doAction(Bot bot, CommandContext context){
        return action.doAction(bot, context, namedParameters.resolve(context));
    }
}
