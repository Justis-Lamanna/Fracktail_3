package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class SequenceAction implements Action {

    private final List<Action> subActions;

    public SequenceAction(List<Action> subActions) {
        this.subActions = subActions;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return Flux.fromIterable(subActions)
                .concatMap(a -> a.doAction(bot, context))
                .last();
    }
}
