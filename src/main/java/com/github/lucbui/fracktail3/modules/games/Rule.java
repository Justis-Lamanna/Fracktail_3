package com.github.lucbui.fracktail3.modules.games;

/**
 * Encapsulates a legal board move
 * @param <T> The piece being moved
 */
public interface Rule<T, B extends Board<T>> {
    /**
     * Check if this is a legal move
     * @param action The action to check the legality of
     * @param board The board being used
     * @return True, if this is a legal move
     */
    ActionLegality isLegalMove(Action<T, B> action, B board);

    /**
     * Add an exception to this rule
     * @param exception The exception to the rule
     * @return A rule which returns true if this rule passes, or any exception also passes
     */
    default Rule<T, B> withException(Rule<T, B> exception) {
        if(this instanceof ComplexRule) {
            ((ComplexRule<T, B>) this).addException(exception);
            return this;
        } else {
            ComplexRule<T, B> complexRule = new ComplexRule<>(this);
            complexRule.addException(exception);
            return this;
        }
    }
}
