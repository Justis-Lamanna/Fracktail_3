package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.Validated;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A list of multiple action options, which are chosen based on a filter
 */
public class ActionOptions implements Action, Validated {
    private final List<ActionOption> actions;
    private final Action _default;

    /**
     * Initialize this list of options
     * @param actions The different arms of the action
     * @param _default A default action, if no others match
     */
    public ActionOptions(List<ActionOption> actions, Action _default) {
        this.actions = Objects.requireNonNull(actions);
        this._default = Objects.requireNonNull(_default);
    }

    /**
     * Get action options
     * @return The list of possible actions
     */
    public List<ActionOption> getActions() {
        return actions;
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
        return Flux.fromIterable(actions)
                .filterWhen(ao -> ao.getFilter().matches(bot, ctx))
                .next()
                .flatMap(ao -> ao.getAction().doAction(bot, ctx))
                .switchIfEmpty(_default.doAction(bot, ctx));
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        actions.forEach(a -> a.validate(botSpec));
        if(_default instanceof Validated) {
            ((Validated) _default).validate(botSpec);
        }
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
            actions.add(new ActionOption(filter, action));
            return this;
        }

        /**
         * Set the action to occur if no others match
         * @param action The action to occur
         * @return This builder
         */
        public Builder ifNoneMatch(Action action) {
            _default = action;
            return this;
        }

        @Override
        public ActionOptions build() {
            return new ActionOptions(actions, _default);
        }
    }
}
