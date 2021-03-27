package com.github.lucbui.fracktail3.modules.games.standard.rule;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.ActionLegality;
import com.github.lucbui.fracktail3.modules.games.Rule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A complex rule, which contains a base rule, and exceptions to the rule
 * If the base rule fails, and one of the exceptions pass, then this rule passes. Otherwise, it fails
 * @param <GF> The game field
 */
@Data
public class ComplexRule<GF> implements Rule<GF> {
    private final Rule<GF> baseRule;
    private final List<Rule<GF>> exceptions = new ArrayList<>();

    /**
     * Add an exception to this rule
     * @param exception The exception to add
     */
    public void addException(Rule<GF> exception) {
        this.exceptions.add(exception);
    }

    /**
     * Remove this exception to the rule
     * @param exception The exception to remove
     * @return True, if the exception was removed
     */
    public boolean removeException(Rule<GF> exception) {
        return this.exceptions.remove(exception);
    }

    @Override
    public ActionLegality isLegalMove(Action<GF> action, GF board) {
        ActionLegality baseMoveLegality = baseRule.isLegalMove(action, board);
        if(baseMoveLegality.isLegal()) {
            return baseMoveLegality;
        }
        boolean isLegalByExceptions = exceptions.stream()
                .map(rule -> rule.isLegalMove(action, board))
                .noneMatch(ActionLegality::isIllegal);
        return isLegalByExceptions ? ActionLegality.legal() : ActionLegality.illegal(baseMoveLegality.getReason());
    }
}
