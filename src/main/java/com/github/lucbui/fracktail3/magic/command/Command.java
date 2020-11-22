package com.github.lucbui.fracktail3.magic.command;

import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.PlatformBasicAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Encapsulation of a bot's command
 */
public class Command implements Id, Disableable {
    private final String id;
    private final Set<String> names;
    private final FormattedString help;
    private final CommandAction action;

    private boolean enabled;

    /**
     * Creates a minimal command
     * The name is the same as the ID, no filter is provided, and help text is "id.help".
     * @param id The command's ID
     * @param action The action to perform
     */
    public Command(String id, CommandAction action) {
        this(id, Collections.singleton(id), action);
    }

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param name The name this command responds to
     * @param action The action to perform when the command is run
     */
    public Command(String id, String name, CommandAction action) {
        this(id, Collections.singleton(name), FormattedString.from(id + ".help"), action);
    }

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param names The name(s) this command responds to
     * @param action The action to perform when the command is run
     */
    public Command(String id, Set<String> names, CommandAction action) {
        this(id, names, FormattedString.from(id + ".help"), action);
    }

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param names The name(s) this command responds to
     * @param help The help text
     * @param action The action to perform when the command is run
     */
    public Command(String id, Set<String> names, FormattedString help, CommandAction action) {
        this(id, true, names, help, action);
    }

    /**
     * Creates a Command
     * @param id The ID of the command
     * @param enabled If this command is enabled or not
     * @param names The name(s) this command responds to
     * @param help The help text
     * @param action The action to perform when the command is run
     */
    public Command(String id, boolean enabled, Set<String> names, FormattedString help, CommandAction action) {
        this.id = id;
        this.enabled = enabled;
        this.names = names;
        this.help = help;
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
     * Get the action this command executes
     * @return The action
     */
    public CommandAction getAction() {
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
     * @param ctx The context of the commands usage
     * @return Asynchronous boolean indicating if the guard passes
     */
    public Mono<Boolean> matches(PlatformBaseContext<?> ctx) {
        return BooleanUtils.and(Mono.just(enabled), action.guard(ctx));
    }

    /**
     * Unconditionally perform the action
     * @param ctx The context of the commands usage
     * @return Asynchronous marker indicating the action finished
     */
    public Mono<Void> doAction(CommandUseContext<?> ctx) {
        return action.doAction(ctx);
    }

    /**
     * Perform action if guard passes
     * @param ctx The context of the commands usage
     * @return Asynchronous marker indicating the action finished
     */
    public Mono<Void> doActionIfPasses(CommandUseContext<?> ctx) {
        return matches(ctx)
                .flatMap(b -> b ? doAction(ctx) : Mono.empty());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Builder class to generate Commands
     */
    public static class Builder implements IBuilder<Command> {
        private final String id;
        private boolean enabled = true;
        private Set<String> names = new HashSet<>();
        private CommandAction action = CommandAction.NOOP;
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
         * Add multiple names to the list
         * @param names The names to add
         * @return This builder
         */
        public Builder withNames(List<String> names) {
            this.names.addAll(names);
            return this;
        }

        /**
         * Add multiple names to the list
         * @param names The names to add
         * @return This builder
         */
        public Builder withNames(String... names) {
            return withNames(Arrays.asList(names));
        }

        /**
         * Set the action of this command
         * @param action The action to perform
         * @return This builder
         */
        public Builder withAction(CommandAction action) {
            this.action = action;
            return this;
        }

        /**
         * Set the action of this command
         * @param action The action to perform
         * @return This builder
         */
        public Builder withAction(PlatformBasicAction action) {
            this.action = action::doAction;
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
        public Builder withAction(IBuilder<? extends CommandAction> actionBuilder) {
            return withAction(actionBuilder.build());
        }

        /**
         * Set if this command is enabled
         * @param enabled True if enabled, false if disabled
         * @return This builder
         */
        public Builder isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Override
        public Command build() {
            if(names.isEmpty()) {
                names = Collections.singleton(id);
            }
            return new Command(id, enabled, names, help, action);
        }
    }
}
