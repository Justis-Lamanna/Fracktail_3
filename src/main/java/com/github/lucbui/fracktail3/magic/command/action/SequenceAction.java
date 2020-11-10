package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Perform several actions in sequence.
 * If any actions are guarded, they are skipped in the sequence.
 * This action is guarded if all of its subactions are guarded.
 */
public class SequenceAction implements CommandAction {

    private final List<CommandAction> subActions;

    /**
     * Initialize the sequential actions
     * @param subActions The actions to perform
     */
    public SequenceAction(List<CommandAction> subActions) {
        this.subActions = subActions;
    }

    /**
     * Get the action sequence
     * @return Action sequence
     */
    public List<CommandAction> getSubActions() {
        return subActions;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return Flux.fromIterable(subActions)
                .filterWhen(c -> c.guard(context))
                .concatMap(a -> a.doAction(context))
                .then();
    }

    @Override
    public Mono<Boolean> guard(CommandUseContext<?> context) {
        return Flux.fromIterable(getSubActions())
                .filterWhen(c -> c.guard(context))
                .hasElements();
    }

    /**
     * Builder which creates a sequence action
     */
    public static class Builder implements IBuilder<SequenceAction> {
        private final List<CommandAction> subActions;

        /**
         * Initialize the Builder
         * @param first The first action to perform
         */
        public Builder(CommandAction first) {
            subActions = new ArrayList<>();
            subActions.add(first);
        }

        /**
         * Set the next action to be performed
         * @param next The next action
         * @return This builder
         */
        public Builder then(CommandAction next) {
            subActions.add(next);
            return this;
        }

        @Override
        public SequenceAction build() {
            return new SequenceAction(subActions);
        }
    }
}
