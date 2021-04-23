package com.github.lucbui.fracktail3.magic.command;

import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.TypeDescriptor;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Encapsulation of a bot's command
 */
@Data
public class Command implements Id {
    private final String id;
    private final Set<String> names;
    private final String help;
    private final CommandAction action;
    private final List<Parameter> parameters;

    /**
     * Test if this guard matches
     * @param ctx The context of the commands usage
     * @return Asynchronous boolean indicating if the guard passes
     */
    public Mono<Boolean> matches(CommandUseContext ctx) {
        return Mono.just(true); //TODO: Remove this call
    }

    /**
     * Unconditionally perform the action
     * @param ctx The context of the commands usage
     * @return Asynchronous marker indicating the action finished
     */
    public Mono<Void> doAction(CommandUseContext ctx) {
        return action.doAction(ctx);
    }

    @Data
    @RequiredArgsConstructor
    public static class Parameter {
        private final String name;
        private final String description;
        private final TypeDescriptor type;
        private final boolean optional;

        public Parameter(String name, String description, Class<?> clazz, boolean optional) {
            this(name, description, TypeDescriptor.valueOf(clazz), optional);
        }
    }

    /**
     * Builder class to generate Commands
     */
    public static class Builder implements IBuilder<Command> {
        private final String id;
        private Set<String> names = new HashSet<>();
        private CommandAction action = CommandAction.NOOP;
        private String help;
        private List<Parameter> parameters = new ArrayList<>();

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
         * Set the help text of this command.
         * By default, this help is formatted with the default formatter
         * @param helpText The help text
         * @return This builder
         */
        public Builder withHelp(String helpText) {
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
         * Add a parameter to this command
         * @param parameter The parameter
         * @return This builder
         */
        public Builder withParameter(Parameter parameter) {
            this.parameters.add(parameter);
            return this;
        }

        @Override
        public Command build() {
            if(names.isEmpty()) {
                names = Collections.singleton(id);
            }
            return new Command(id, names, help, action, parameters);
        }
    }
}
