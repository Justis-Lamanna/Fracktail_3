package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import com.github.lucbui.fracktail3.magic.utils.model.IdStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A list of multiple action options, which are chosen based on a filter
 */
public class ActionOptions extends IdStore<ActionOption> implements Action, Validated {
    private final Action _default;

    /**
     * Initialize this list of options
     * @param actions The different arms of the action
     * @param _default A default action, if no others match
     */
    public ActionOptions(List<ActionOption> actions, Action _default) {
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
    public Action getDefault() {
        return _default;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext ctx) {
        return Flux.fromIterable(getAll())
                .filterWhen(ao -> ao.passesFilter(bot, ctx))
                .map(ActionOption::getAction)
                .next().defaultIfEmpty(_default)
                .flatMap(action -> action.doAction(bot, ctx));
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        getAll().forEach(a -> a.validate(botSpec));
        Validated.validate(_default, botSpec);
    }

    /**
     * Builder which allows for easier construction
     */
    public static class Builder implements IBuilder<ActionOptions> {
        private final List<ActionOption> actions = new ArrayList<>();
        private Action _default = Action.NOOP;

        /**
         * Add an arm and possible action
         * @param filter The filter that must pass
         * @param action The action that should occur
         * @return This builder
         */
        public Builder with(Filter filter, Action action) {
            return with(true, filter, action);
        }

        /**
         * Add an arm and possible action
         * @param filter The filter that must pass
         * @param action The action that should occur
         * @return This builder
         */
        public Builder with(boolean enabled, Filter filter, Action action) {
            actions.add(new ActionOption("action_" + actions.size(), enabled, filter, action));
            return this;
        }

        /**
         * Set the action to occur if no others match
         * @param action The action to occur
         * @return This builder
         */
        public Builder orElseDo(Action action) {
            _default = action;
            return this;
        }

        @Override
        public ActionOptions build() {
            return new ActionOptions(actions, _default);
        }
    }
}
