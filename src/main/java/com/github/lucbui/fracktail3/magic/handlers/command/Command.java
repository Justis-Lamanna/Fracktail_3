package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulation of a bot's command
 */
public class Command implements Validated, Id {
    private final String id;
    private final Set<String> names;
    private final FormattedString help;
    private final Filter commandFilter;
    private final Action action;

    /**
     * Creates a minimal command
     * The name is the same as the ID, no filter is provided, and help text is "id.help".
     * @param id The command's ID
     * @param action The action to perform
     */
    public Command(String id, Action action) {
        this(id, Filter.identity(true), action);
    }

    /**
     * Creates a minimal command
     * The name is the same as the ID, no filter is provided, and help text is "id.help".
     * @param id The command's ID
     * @param filter A filter for this command
     * @param action The action to perform
     */
    public Command(String id, Filter filter, Action action) {
        this(id, Collections.singleton(id), filter, action);
    }

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param name The name this command responds to
     * @param filter A guard, which prevents this command from being used in certain contexts
     * @param action The action to perform when the command is run
     */
    public Command(String id, String name, Filter filter, Action action) {
        this(id, Collections.singleton(name), FormattedString.from(id + ".help"), filter, action);
    }

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param names The name(s) this command responds to
     * @param filter A guard, which prevents this command from being used in certain contexts
     * @param action The action to perform when the command is run
     */
    public Command(String id, Set<String> names, Filter filter, Action action) {
        this(id, names, FormattedString.from(id + ".help"), filter, action);
    }

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param names The name(s) this command responds to
     * @param help The help text
     * @param filter A guard, which prevents this command from being used in certain contexts
     * @param action The action to perform when the command is run
     */
    public Command(String id, Set<String> names, FormattedString help, Filter filter, Action action) {
        this.id = id;
        this.names = names;
        this.help = help;
        this.commandFilter = filter;
        this.action = action;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Get the command names
     * @return The command names
     */
    public Set<String> getNames() {
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
     * Get the help text of this command
     * @return The help text
     */
    public FormattedString getHelp() {
        return help;
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
        private Set<String> names = new HashSet<>();
        private Filter filter = Filter.identity(true);
        private Action action = Action.NOOP;
        private FormattedString help;

        /**
         * Initialize Builder with Command ID
         * @param id The ID of the command
         */
        public Builder(String id) {
            this.id = id;
        }

        /**
         * Add a name to the list of command names.
         * By default, this name is formatted with the default formatter
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
         * Set the help text of this command.
         * By default, this help is formatted with the default formatter
         * @param helpText The help text
         * @return This builder
         */
        public Builder withHelp(String helpText) {
            this.help = FormattedString.from(helpText);
            return this;
        }

        /**
         * Set the help text of this command
         * @param helpText The help text
         * @return This builder
         */
        public Builder withHelp(FormattedString helpText) {
            this.help = helpText;
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
                names = Collections.singleton(id);
            }
            if(help == null) {
                this.help = FormattedString.from(id + ".help");
            }
            return new Command(id, names, help, filter, action);
        }
    }
}
