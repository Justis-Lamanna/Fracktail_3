package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
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

    public static class Builder {
        private final List<Pair<Action, Double>> actions;

        public Builder() {
            this.actions = new ArrayList<>();
        }

        public Builder add(Action action, double weight) {
            actions.add(Pair.create(action, weight));
            return this;
        }

        public RandomAction build() {
            return new RandomAction(new EnumeratedDistribution<>(actions));
        }
    }
}
