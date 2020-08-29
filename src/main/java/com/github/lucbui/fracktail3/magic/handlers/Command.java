package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.magic.role.Roleset;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(Command.class);
    private static final Resolver<List<String>> EMPTY_LIST_RESOLVER = Resolver.identity(Collections.emptyList());

    private final String id;
    private final Resolver<List<String>> names;
    private final String role;
    private final List<Behavior> behaviors;
    private final Action orElse;

    private boolean enabled;

    public Command(
            String id,
            Resolver<List<String>> names,
            String role,
            List<Behavior> behaviors,
            Action orElse) {
        this.id = id;
        this.names = names;
        this.role = role;
        this.behaviors = behaviors;
        this.orElse = orElse;
        this.enabled = true;
    }

    public String getId() {
        return id;
    }

    public Resolver<List<String>> getNames() {
        return names;
    }

    public String getRole() {
        return role;
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public Action getOrElse() {
        return orElse;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Mono<Boolean> matchesRole(BotSpec botSpec, CommandContext context) {
        if(hasRoleRestriction()) {
            Roleset roleset = botSpec.getRoleset(role)
                    .orElseThrow(() -> new CommandUseException("Unknown Roleset: " + role));
            return roleset.validateInRole(botSpec, context);
        }
        return Mono.just(true);
    }

    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return Flux.fromIterable(behaviors)
                .filter(Behavior::isEnabled)
                .filterWhen(b -> b.matchesRole(bot.getSpec(), context))
                .filterWhen(b -> b.matchesParameterCount(bot.getSpec(), context))
                .next()
                .flatMap(behavior -> behavior.doAction(bot, context).thenReturn(true))
                .switchIfEmpty(orElse == null ? Mono.empty() : orElse.doAction(bot, context).thenReturn(true))
                .then();
    }

    public boolean hasRoleRestriction() {
        return StringUtils.isNotBlank(role);
    }
}
