package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class ActionOptions {
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

    public Mono<Void> doAction(Bot bot, CommandContext<?> ctx) {
        return Flux.fromIterable(actions)
                .filterWhen(ao -> ao.getFilter().matches(bot, ctx))
                .next()
                .flatMap(ao -> ao.getAction().doAction(bot, ctx))
                .switchIfEmpty(_default.doAction(bot, ctx));
    }
}
