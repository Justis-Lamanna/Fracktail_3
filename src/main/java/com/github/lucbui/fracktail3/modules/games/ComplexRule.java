package com.github.lucbui.fracktail3.modules.games;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ComplexRule<T, B extends Board<T>> implements Rule<T, B> {
    public final Rule<T, B> baseRule;
    public final List<Rule<T, B>> exceptions = new ArrayList<>();

    /**
     * Add an exception to this rule
     * @param exception The exception to add
     */
    public void addException(Rule<T, B> exception) {
        this.exceptions.add(exception);
    }

    /**
     * Remove this exception to the rule
     * @param exception The exception to remove
     * @return True, if the exception was removed
     */
    public boolean removeException(Rule<T, B> exception) {
        return this.exceptions.remove(exception);
    }

    @Override
    public ActionLegality isLegalMove(Action<T, B> action, B board) {
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
