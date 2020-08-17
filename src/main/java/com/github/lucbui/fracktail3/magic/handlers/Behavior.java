package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import com.github.lucbui.fracktail3.magic.role.Roleset;
import com.github.lucbui.fracktail3.magic.utils.MonoUtils;
import org.apache.commons.lang3.StringUtils;
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

    public int getParamCount() {
        return paramCount;
    }

    public NamedParametersConfiguration getNamedParameters() {
        return namedParameters;
    }

    public String getRole() {
        return role;
    }

    public Action getAction() {
        return action;
    }

    public Mono<Boolean> matches(Bot bot, CommandContext context) {
        if(hasRoleRestriction()) {
            Roleset set = bot.getRolesets()
                    .flatMap(r -> r.getRoleset(role))
                    .orElseThrow(() -> new CommandUseException("Behavior uses unknown role " + role));
            return MonoUtils.and(paramCountMatches(context), set.validateInRole(bot, context));
        } else {
            return paramCountMatches(context);
        }
    }

    private Mono<Boolean> paramCountMatches(CommandContext context) {
        return Mono.just(this.paramCount == -1 || context.getNormalizedParameters().length == this.paramCount);
    }

    public Mono<Void> doAction(Bot bot, CommandContext context){
        LOGGER.info("Performing action: {}", action);
        return action.doAction(bot, context, namedParameters.resolve(context));
    }

    public boolean hasRoleRestriction() {
        return StringUtils.isNotBlank(role);
    }
}
