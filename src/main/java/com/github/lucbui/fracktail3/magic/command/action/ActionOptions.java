package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import com.github.lucbui.fracktail3.magic.util.IdStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A list of multiple action options, which are chosen based on a filter.
 * This element's guard returns false if all contained action guards return false (or are disabled).
 */
public class ActionOptions extends IdStore<ActionOption> implements CommandAction {
    private final CommandAction _default;

    /**
     * Initialize this list of options
     * @param actions The different arms of the action
     * @param _default A default action, if no others match
     */
    public ActionOptions(List<ActionOption> actions, CommandAction _default) {
        super(actions);
        this._default = Objects.requireNonNull(_default);
    }

    /**
     * Get action options
     * @return The list of possible actions
     */
    public List<ActionOption> getActions() {
        return getAll();
    }

    /**
     * Get the default action
     * @return Default action
     */
    public CommandAction getDefault() {
        return _default;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return Flux.fromIterable(getAll())
                .filterWhen(ao -> ao.matches(context))
                .map(ActionOption::getAction)
                .next().defaultIfEmpty(_default)
                .flatMap(action -> action.doAction(context));
    }

    @Override
    public Mono<Boolean> guard(CommandUseContext<?> context) {
        return Flux.fromIterable(getAll())
                .filterWhen(ao -> ao.matches(context))
                .hasElements();
    }

    /**
     * Builder which allows for easier construction
     */
    public static class Builder implements IBuilder<ActionOptions> {
        private final List<ActionOption> actions = new ArrayList<>();
        private CommandAction _default = CommandAction.NOOP;

        /**
         * Add an arm and possible action
         * @param action The action that should occur
         * @return This builder
         */
        public Builder with(CommandAction action) {
            return with(true, action);
        }

        /**
         * Add an arm and possible action
         * @param action The action that should occur
         * @return This builder
         */
        public Builder with(boolean enabled, CommandAction action) {
            actions.add(new ActionOption("action_" + actions.size(), enabled, action));
            return this;
        }

        /**
         * Set the action to occur if no others match
         * @param action The action to occur
         * @return This builder
         */
        public Builder orElseDo(CommandAction action) {
            _default = action;
            return this;
        }

        @Override
        public ActionOptions build() {
            return new ActionOptions(actions, _default);
        }
    }
}
