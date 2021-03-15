package com.github.lucbui.fracktail3.modules.games;

import com.github.lucbui.fracktail3.modules.games.exceptions.IllegalActionException;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * A game session
 * @param <GF> The playing field
 */
@Data
public class Game<GF> {
    private final List<Rule<GF>> rulesList;
    private final GF gameField;

    /**
     * Perform a potentially-illegal action
     * @param action The action to attempt to perform
     * @throws IllegalActionException If the attempted action is illegal
     */
    public void performAction(Action<GF> action) {
        ActionLegality legality = canPerformAction(action);
        if(legality.isLegal()) {
            action.performAction(this);
        } else {
            throw new IllegalActionException(legality);
        }
    }

    /**
     * Check if the action can be performed
     * @param action The action to attempt
     * @return A legal action, or an illegal action + a reason for its illegality
     */
    public ActionLegality canPerformAction(Action<GF> action) {
        Optional<ActionLegality> illegal = rulesList.stream()
                .map(rule -> rule.isLegalMove(action, gameField))
                .filter(ActionLegality::isIllegal)
                .findFirst();
        return illegal.orElse(ActionLegality.legal());
    }
}
