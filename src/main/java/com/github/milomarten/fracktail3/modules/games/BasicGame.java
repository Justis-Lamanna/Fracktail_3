package com.github.milomarten.fracktail3.modules.games;

import com.github.milomarten.fracktail3.modules.games.exceptions.IllegalActionException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Data
public abstract class BasicGame<GF> implements Game<GF> {
    private final List<Rule<GF>> rulesList;
    private final GF gameField;

    @Getter(AccessLevel.PROTECTED)
    private final Queue<Action<GF>> actions = new LinkedList<>();

    private final Sinks.Many<ActionData<GF>> actionsSink =
            Sinks.many().replay().latest();

    /**
     * Perform a potentially-illegal action
     * @param action The action to attempt to perform
     * @throws IllegalActionException If the attempted action is illegal
     */
    @Override
    public void performAction(Action<GF> action) {
        ActionLegality legality = canPerformAction(action);
        if(legality.isLegal()) {
            getActions().add(action);
            action.performAction(gameField);
            actionsSink.tryEmitNext(new ActionData<>(gameField, action));

            if(isComplete()) {
                actionsSink.tryEmitComplete();
            }
        } else {
            throw new IllegalActionException(legality);
        }
    }

    /**
     * Check if the action can be performed
     * @param action The action to attempt
     * @return A legal action, or an illegal action + a reason for its illegality
     */
    @Override
    public ActionLegality canPerformAction(Action<GF> action) {
        Optional<ActionLegality> illegal = rulesList.stream()
                .map(rule -> rule.isLegalMove(action, gameField))
                .filter(ActionLegality::isIllegal)
                .findFirst();
        return illegal.orElse(ActionLegality.legal());
    }

    /**
     * A feed of actions that come as players play
     * @return A flux which plays the actions
     */
    public Flux<ActionData<GF>> actionsFeed() {
        return actionsSink.asFlux();
    }

    @Data
    public static class ActionData<GF> {
        private final GF postAction;
        private final Action<GF> action;
    }
}
