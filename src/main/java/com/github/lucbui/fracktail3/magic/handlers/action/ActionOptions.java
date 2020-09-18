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

public class ActionOptions implements Validated {
    private final List<ActionOption> actions;
    private final Action _default;

    public ActionOptions(List<ActionOption> actions, Action _default) {
        this.actions = Objects.requireNonNull(actions);
        this._default = Objects.requireNonNull(_default);
    }

    public List<ActionOption> getActions() {
        return actions;
    }

    public Action getDefault() {
        return _default;
    }

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
        _default.validate(botSpec);
    }

    public static class Builder implements IBuilder<ActionOptions> {
        private final List<ActionOption> actions = new ArrayList<>();
        private Action _default = Action.NOOP;

        public Builder with(Filter filter, Action action) {
            actions.add(new ActionOption(filter, action));
            return this;
        }

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
