package com.github.lucbui.fracktail3.modules.games;

import com.github.lucbui.fracktail3.modules.games.exceptions.IllegalActionException;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class Game<T, B extends Board<T>> {
    private final List<Rule<T, B>> rulesList;
    private final B board;

    public void performAction(Action<T, B> action) {
        ActionLegality legality = canPerformAction(action);
        if(legality.isLegal()) {
            action.performAction(this);
        } else {
            throw new IllegalActionException(legality);
        }
    }

    public ActionLegality canPerformAction(Action<T, B> action) {
        Optional<ActionLegality> illegal = rulesList.stream()
                .map(rule -> rule.isLegalMove(action, board))
                .filter(ActionLegality::isIllegal)
                .findFirst();
        return illegal.orElse(ActionLegality.legal());
    }
}
