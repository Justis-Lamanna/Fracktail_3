package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.role.Roleset;
import com.github.lucbui.fracktail3.magic.utils.Range;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Behavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(Behavior.class);

    private final Range paramCount;
    private final String role;
    private final Action action;

    public Behavior(Range paramCount, Action action, String role) {
        this.paramCount = paramCount;
        this.role = role;
        this.action = action;
    }

    public Behavior(Range paramCount, Action action) {
        this(paramCount, action, null);
    }

    public Behavior(int paramCount, Action action) {
        this(Range.single(paramCount), action);
    }

    public Behavior(Action action) {
        this(Range.unbounded(), action);
    }

    public Range getParamCount() {
        return paramCount;
    }

    public String getRole() {
        return role;
    }

    public Action getAction() {
        return action;
    }

    public Mono<Boolean> matchesRole(BotSpec botSpec, CommandContext context) {
        if(hasRoleRestriction()) {
            Roleset roleset = botSpec.getRoleset(role)
                    .orElseThrow(() -> new CommandUseException("Unknown Roleset: " + role));
            return roleset.validateInRole(botSpec, context);
        }
        return Mono.just(true);
    }

    public Mono<Boolean> matchesParameterCount(BotSpec botSpec, CommandContext context) {
        return Mono.just(paramCount.isInside(context.getNormalizedParameters().length));
    }

    public Mono<Void> doAction(Bot bot, CommandContext context){
        LOGGER.info("Performing action: {}", action);
        return action.doAction(bot, context);
    }

    public boolean hasRoleRestriction() {
        return StringUtils.isNotBlank(role);
    }
}
