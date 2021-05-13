package com.github.lucbui.fracktail3.magic.command;

import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.guard.Guard;
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
    private final Guard restriction;
    private final CommandAction action;
    private final List<Parameter> parameters;

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
        private final int index;
        private final String name;
        private final String description;
        private final TypeDescriptor type;
        private final boolean optional;

        public Parameter(int index, String name, String description, Class<?> clazz, boolean optional) {
            this(index, name, description, TypeDescriptor.valueOf(clazz), optional);
        }
    }

    /**
     * Builder class to generate Commands
     */
    public static class Builder implements IBuilder<Command> {
        private final String id;
        private Set<String> names = new HashSet<>();
        private Guard guard = null;
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
         * Set the guard this command uses
         * @param guard An asynchronous predicate which indicates this method is restricted in use
         * @return This builder
         */
        public Builder withRestriction(Guard guard) {
            this.guard = guard;
            return this;
        }

        /**
         * Add an additional guard, with the previous guard and this one and'ed together
         * If no previous guard exists, the supplied guard becomes the new one.
         * @param guard The guard to and
         * @return This builder
         */
        public Builder andRestriction(Guard guard) {
            if(this.guard == null) {
                return withRestriction(guard);
            }
            this.guard = this.guard.and(guard);
            return this;
        }

        /**
         * Add an additional guard, with the previous guard and this one or'ed together
         * If no previous guard exists, the supplied guard becomes the new one.
         * @param guard The guard to or
         * @return This builder
         */
        public Builder orRestriction(Guard guard) {
            if(this.guard == null) {
                return withRestriction(guard);
            }
            this.guard = this.guard.or(guard);
            return this;
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
            if(guard == null) {
                guard = Guard.identity(true);
            }
            return new Command(id, names, help, guard, action, parameters);
        }
    }
}
