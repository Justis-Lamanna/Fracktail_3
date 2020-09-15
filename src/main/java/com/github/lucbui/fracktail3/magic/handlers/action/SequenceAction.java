package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.utils.IBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class SequenceAction implements Action {

    private final List<Action> subActions;

    public SequenceAction(List<Action> subActions) {
        this.subActions = subActions;
    }

    public List<Action> getSubActions() {
        return subActions;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return Flux.fromIterable(subActions)
                .concatMap(a -> a.doAction(bot, context))
                .last();
    }

    public static class Builder implements IBuilder<SequenceAction> {
        private final List<Action> subActions;

        public Builder(Action first) {
            subActions = new ArrayList<>();
            subActions.add(first);
        }

        public Builder then(Action next) {
            subActions.add(next);
            return this;
        }

        @Override
        public SequenceAction build() {
            return new SequenceAction(subActions);
        }
    }
}
