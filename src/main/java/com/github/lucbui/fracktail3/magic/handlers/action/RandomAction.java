package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class RandomAction implements Action {
    private final EnumeratedDistribution<Action> actions;

    public RandomAction(EnumeratedDistribution<Action> actions) {
        this.actions = actions;
    }

    public EnumeratedDistribution<Action> getActions() {
        return actions;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return actions.sample().doAction(bot, context);
    }

    public static class Builder implements IBuilder<RandomAction> {
        private final List<Pair<Action, Double>> actions;

        public Builder() {
            this.actions = new ArrayList<>();
        }

        public Builder with(double weight, Action action) {
            actions.add(Pair.create(action, weight));
            return this;
        }

        public Builder with(Action action) {
            return with(1, action);
        }

        @Override
        public RandomAction build() {
            return new RandomAction(new EnumeratedDistribution<>(actions));
        }
    }
}
