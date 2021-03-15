package com.github.lucbui.fracktail3.modules.games;

/**
 * Encapsulates a legal board move
 * @param <GF> The Game Field being applied
 */
public interface Rule<GF> {
    /**
     * Check if this is a legal move
     * @param action The action to check the legality of
     * @param board The board being used
     * @return True, if this is a legal move
     */
    ActionLegality isLegalMove(Action<GF> action, GF board);

    /**
     * Add an exception to this rule
     * @param exception The exception to the rule
     * @return A rule which returns true if this rule passes, or any exception also passes
     */
    default Rule<GF> withException(Rule<GF> exception) {
        if(this instanceof ComplexRule) {
            ((ComplexRule<GF>) this).addException(exception);
            return this;
        } else {
            ComplexRule<GF> complexRule = new ComplexRule<>(this);
            complexRule.addException(exception);
            return this;
        }
    }
}
