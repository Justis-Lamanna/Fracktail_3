package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulation of a bot's command
 */
public class Command implements Validated, Id {
    private final String id;
    private final List<String> names;
    private final Filter commandFilter;
    private final Action action;

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param names The name(s) this command responds to
     * @param commandFilter A guard, which prevents this command from being used in certain contexts
     * @param action The action to perform when the command is run
     */
    public Command(String id, List<String> names, Filter commandFilter, Action action) {
        this.id = Objects.requireNonNull(id);
        this.names = Objects.requireNonNull(names);
        this.commandFilter = Objects.requireNonNull(commandFilter);
        this.action = Objects.requireNonNull(action);
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Get the command names
     * @return The command names
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * Get the filter this command uses
     * @return The filter
     */
    public Filter getFilter() {
        return commandFilter;
    }

    /**
     * Get the action this command executes
     * @return The action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Test if this guard matches
     * @param bot The bot being executed
     * @param ctx The context of the commands usage
     * @return Asynchronous boolean indicating if the guard passes
     */
    public Mono<Boolean> passesFilter(Bot bot, CommandContext ctx) {
        return commandFilter.matches(bot, ctx);
    }

    /**
     * Unconditionally perform the action
     * @param bot The bot being executed
     * @param ctx The context of the commands usage
     * @return Asynchronous marker indicating the action finished
     */
    public Mono<Void> doAction(Bot bot, CommandContext ctx) {
        return action.doAction(bot, ctx);
    }

    /**
     * Perform action if guard passes
     * @param bot The bot being executed
     * @param ctx The context of the commands usage
     * @return Asynchronous marker indicating the action finished
     */
    public Mono<Void> doActionIfPasses(Bot bot, CommandContext ctx) {
        return passesFilter(bot, ctx)
                .flatMap(b -> b ? doAction(bot, ctx) : Mono.empty());
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        if(commandFilter instanceof Validated) {
            ((Validated) commandFilter).validate(botSpec);
        }
        if(action instanceof Validated) {
            ((Validated) action).validate(botSpec);
        }
    }

    /**
     * Builder class to generate Commands
     */
    public static class Builder implements IBuilder<Command> {
        private final String id;
        private List<String> names = new ArrayList<>();
        private Filter filter = Filter.identity(true);
        private Action action = Action.NOOP;

        /**
         * Initialize Builder with Command ID
         * @param id The ID of the command
         */
        public Builder(String id) {
            this.id = id;
        }

        /**
         * Add a name to the list of command names.
         * @param name The name to add
         * @return This builder
         */
        public Builder withName(String name) {
            names.add(name);
            return this;
        }

        /**
         * Set the filter of this command
         * @param filter The filter to use
         * @return This builder
         */
        public Builder withFilter(Filter filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Set the action of this command
         * @param action The action to perform
         * @return This builder
         */
        public Builder withAction(Action action) {
            this.action = action;
            return this;
        }

        /**
         * Set the action of this command
         * @param actionBuilder An ActionBuilder, which is resolved
         * @return This builder
         */
        public Builder withAction(IBuilder<? extends Action> actionBuilder) {
            return withAction(actionBuilder.build());
        }

        @Override
        public Command build() {
            if(names.isEmpty()) {
                names = Collections.singletonList(id);
            }
            return new Command(
                id,
                names,
                filter,
                action
            );
        }
    }
}
