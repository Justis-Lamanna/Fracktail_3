package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Perform several actions in sequence
 */
public class SequenceAction implements Action {

    private final List<Action> subActions;

    /**
     * Initialize the sequential actions
     * @param subActions The actions to perform
     */
    public SequenceAction(List<Action> subActions) {
        this.subActions = subActions;
    }

    /**
     * Get the action sequence
     * @return Action sequence
     */
    public List<Action> getSubActions() {
        return subActions;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return Flux.fromIterable(subActions)
                .concatMap(a -> a.doAction(context))
                .then();
    }

    /**
     * Builder which creates a sequence action
     */
    public static class Builder implements IBuilder<SequenceAction> {
        private final List<Action> subActions;

        /**
         * Initialize the Builder
         * @param first The first action to perform
         */
        public Builder(Action first) {
            subActions = new ArrayList<>();
            subActions.add(first);
        }

        /**
         * Set the next action to be performed
         * @param next The next action
         * @return This builder
         */
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
