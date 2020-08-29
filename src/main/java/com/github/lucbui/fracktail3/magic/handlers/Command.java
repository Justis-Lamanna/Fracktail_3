package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.magic.role.Roleset;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;

public class Command {
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

    public Mono<Boolean> matchesRole(BotSpec botSpec, CommandContext<?> context) {
        if(hasRoleRestriction()) {
            Roleset roleset = botSpec.getRoleset(role)
                    .orElseThrow(() -> new CommandUseException("Unknown Roleset: " + role));
            return roleset.validateInRole(botSpec, context);
        }
        return Mono.just(true);
    }

    public Mono<Void> doAction(Bot bot, CommandContext<?> context) {
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

    public Resolved resolve(Config config, Locale locale) {
        return new Resolved(this, locale, names.resolve(config, locale));
    }

    public static class Resolved {
        public static final Resolved NONE = new Resolved(null, null, null);

        private final Command command;
        private final Locale locale;
        private final List<String> names;

        public Resolved(Command command, Locale locale, List<String> names) {
            this.command = command;
            this.locale = locale;
            this.names = names;
        }

        public String getId() {
            return command.getId();
        }

        public Locale getLocale() {
            return locale;
        }

        public List<String> getNames() {
            return names;
        }

        public String getRole() {
            return command.getRole();
        }

        public List<Behavior> getBehaviors() {
            return command.getBehaviors();
        }

        public Action getOrElse() {
            return command.getOrElse();
        }

        public boolean isEnabled() {
            return command.isEnabled();
        }

        public Mono<Boolean> matchesRole(BotSpec botSpec, CommandContext<?> context) {
            return command.matchesRole(botSpec, context);
        }

        public Mono<Void> doAction(Bot bot, CommandContext<?> context) {
            return command.doAction(bot, context);
        }

        public boolean hasRoleRestriction() {
            return command.hasRoleRestriction();
        }
    }
}
