package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Perform one of a number of random actions
 * Each action is weighted relative to the sum of all weights. If all weights are the same, all actions are equally
 * likely. When in doubt, weight can be a % value, as long as you maintain a total weight sum of 100
 */
public class RandomAction implements Action, Validated {
    private final EnumeratedDistribution<Action> actions;

    /**
     * Initialize this action with a weighted distribution of actions
     * @param actions The actions to perform
     */
    public RandomAction(EnumeratedDistribution<Action> actions) {
        this.actions = actions;
    }

    /**
     * Get the actions that can be performed, and their weights
     * @return The actions to perform
     */
    public EnumeratedDistribution<Action> getActions() {
        return actions;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return actions.sample().doAction(context);
    }

    @Override
    public void validate(BotSpec spec) throws BotConfigurationException {
        actions.getPmf().forEach(pair -> Validated.validate(pair.getFirst(), spec));
    }

    /**
     * Builder to make RandomAction creation easier
     */
    public static class Builder implements IBuilder<RandomAction> {
        private final RandomGenerator random;
        private final List<Pair<Action, Double>> actions = new ArrayList<>();

        /**
         * Initialize the builder
         */
        public Builder() {
            this.random = null;
        }

        public Builder(RandomGenerator random) {
            this.random = random;

        }

        /**
         * Add a random action
         * @param weight The weight of the action, relative to the others
         * @param action The action to perform
         * @return This builder
         */
        public Builder with(double weight, Action action) {
            actions.add(Pair.create(action, weight));
            return this;
        }

        /**
         * Add a random action with weight 1
         * @param action The action to perform
         * @return This builder
         */
        public Builder with(Action action) {
            return with(1, action);
        }

        @Override
        public RandomAction build() {
            return new RandomAction(random == null ? new EnumeratedDistribution<>(actions) : new EnumeratedDistribution<>(random, actions));
        }
    }
}
