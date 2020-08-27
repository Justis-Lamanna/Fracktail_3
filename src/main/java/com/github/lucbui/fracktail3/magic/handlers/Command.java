package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
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

    public Command(Resolver<String> name, Resolver<List<String>> aliases, List<Behavior> behaviors, Action orElse) {
        this(name, aliases, null, behaviors, orElse);
    }

    public Command(Resolver<String> name, Resolver<List<String>> aliases, String role, List<Behavior> behaviors) {
        this(name, aliases, role, behaviors, null);
    }

    public Command(Resolver<String> name, Resolver<List<String>> aliases, List<Behavior> behaviors) {
        this(name, aliases, null, behaviors, null);
    }

    public Command(Resolver<String> name, List<Behavior> behaviors, Action orElse) {
        this(name, EMPTY_LIST_RESOLVER, null, behaviors, orElse);
    }

    public Command(Resolver<String> name, List<Behavior> behaviors) {
        this(name, EMPTY_LIST_RESOLVER, null, behaviors, null);
    }

    public Command(String name, Behavior behavior) {
        this(Resolver.identity(name), EMPTY_LIST_RESOLVER, null, Collections.singletonList(behavior), null);
    }

    public Command(String name, Action action) {
        this(name, new Behavior(action));
    }

    public Resolver<String> getName() {
        return name;
    }

    public Resolver<List<String>> getAliases() {
        return aliases;
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

    public Mono<Boolean> matchesRole(Bot bot, CommandContext context) {
        if(hasRoleRestriction()) {
            Roleset roleset = bot.getRoleset(role)
                    .orElseThrow(() -> new CommandUseException("Unknown Roleset: " + role));
            return roleset.validateInRole(bot, context);
        }
        return Mono.just(true);
    }

    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return Flux.fromIterable(behaviors)
                .filterWhen(b -> b.matchesRole(bot, context))
                .filterWhen(b -> b.matchesParameterCount(bot, context))
                .next()
                .flatMap(behavior -> behavior.doAction(bot, context).thenReturn(true))
                .switchIfEmpty(orElse == null ? Mono.empty() : orElse.doAction(bot, context).thenReturn(true))
                .then();
    }

    public boolean hasRoleRestriction() {
        return StringUtils.isNotBlank(role);
    }
}
