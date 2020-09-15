package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.resolver.CompositeResolver;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.ListFromI18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.magic.utils.IBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Command implements Validated, Id {
    private final String id;
    private final Resolver<List<String>> names;
    private final Filter commandFilter;
    private final Action action;

    public Command(String id, Resolver<List<String>> names, Filter commandFilter, Action action) {
        this.id = Objects.requireNonNull(id);
        this.names = Objects.requireNonNull(names);
        this.commandFilter = Objects.requireNonNull(commandFilter);
        this.action = Objects.requireNonNull(action);
    }

    @Override
    public String getId() {
        return id;
    }

    public Resolver<List<String>> getNames() {
        return names;
    }

    public Filter getCommandFilter() {
        return commandFilter;
    }

    public Action getActions() {
        return action;
    }

    public Mono<Boolean> matchesTrigger(Bot bot, CommandContext ctx) {
        return commandFilter.matches(bot, ctx);
    }

    public Mono<Void> doAction(Bot bot, CommandContext ctx) {
        return action.doAction(bot, ctx);
    }

    public void validate(BotSpec botSpec) throws BotConfigurationException {
        if(commandFilter instanceof Validated) {
            ((Validated) commandFilter).validate(botSpec);
        }
        action.validate(botSpec);
    }

    public static class Builder implements IBuilder<Command> {
        private String id;
        private Resolver<List<String>> explicitResolver = null;
        private List<Resolver<String>> names = new ArrayList<>();
        private Filter filter = Filter.identity(true);
        private Action action = Action.NOOP;

        public Builder(String id) {
            this.id = id;
        }

        public Builder withName(String name) {
            names.add(Resolver.identity(name));
            return this;
        }

        public Builder withI18NName(String key) {
            explicitResolver = null;
            names.add(new I18NResolver(key));
            return this;
        }

        public Builder withI18NName(String key, String _default) {
            explicitResolver = null;
            names.add(new I18NResolver(key, _default));
            return this;
        }

        public Builder withI18NNames(String key) {
            explicitResolver = new ListFromI18NResolver(key);
            return this;
        }

        public Builder withResolver(Resolver<List<String>> resolver) {
            explicitResolver = resolver;
            return this;
        }

        public Builder withFilter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public Builder withAction(Action action) {
            this.action = action;
            return this;
        }

        public Builder withAction(IBuilder<? extends Action> actionBuilder) {
            return withAction(actionBuilder.build());
        }

        @Override
        public Command build() {
            if(explicitResolver == null && names.isEmpty()) {
                explicitResolver = Resolver.identity(Collections.singletonList(id));
            }
            return new Command(
                    id,
                    explicitResolver == null ? new CompositeResolver<>(names) : explicitResolver,
                    filter,
                    action
            );
        }
    }
}
