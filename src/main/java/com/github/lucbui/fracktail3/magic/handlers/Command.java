package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;

public class Command {
    private final String id;
    private final Resolver<List<String>> names;
    private final CommandTrigger commandTrigger;
    private final List<Behavior> behaviors;
    private final Action orElse;

    public Command(
            String id,
            Resolver<List<String>> names,
            CommandTrigger commandTrigger,
            List<Behavior> behaviors,
            Action orElse) {
        this.id = id;
        this.names = names;
        this.commandTrigger = commandTrigger;
        this.behaviors = behaviors;
        this.orElse = orElse;
    }

    public String getId() {
        return id;
    }

    public Resolver<List<String>> getNames() {
        return names;
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public CommandTrigger getCommandTrigger() {
        return commandTrigger;
    }

    public Action getOrElse() {
        return orElse;
    }

    public Mono<Boolean> matchesTrigger(Bot bot, CommandContext<?> context) {
        return commandTrigger.matches(bot, context);
    }

    public Mono<Void> doAction(Bot bot, CommandContext<?> context) {
        return Flux.fromIterable(behaviors)
                .filterWhen(b -> b.matchesTrigger(bot, context))
                .next()
                .flatMap(behavior -> behavior.doAction(bot, context).thenReturn(true))
                .switchIfEmpty(orElse == null ? Mono.empty() : orElse.doAction(bot, context).thenReturn(true))
                .then();
    }

    public Resolved resolve(Config config, Locale locale) {
        return new Resolved(this, locale, names.resolve(config, locale));
    }

    public static class Resolved {
        private final Command command;
        private final Locale locale;
        private final List<String> names;

        public Resolved(Command command, Locale locale, List<String> names) {
            this.command = command;
            this.locale = locale;
            this.names = names;
        }

        public Locale getLocale() {
            return locale;
        }

        public List<String> getNames() {
            return names;
        }

        public String getId() {
            return command.getId();
        }

        public List<Behavior> getBehaviors() {
            return command.getBehaviors();
        }

        public CommandTrigger getCommandTrigger() {
            return command.getCommandTrigger();
        }

        public Action getOrElse() {
            return command.getOrElse();
        }

        public Mono<Boolean> matchesTrigger(Bot bot, CommandContext<?> context) {
            return command.matchesTrigger(bot, context);
        }

        public Mono<Void> doAction(Bot bot, CommandContext<?> context) {
            return command.doAction(bot, context);
        }
    }
}
