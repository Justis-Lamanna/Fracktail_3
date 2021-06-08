package com.github.milomarten.fracktail3.modules.games;

import com.github.milomarten.fracktail3.modules.games.exceptions.IllegalActionException;

/**
 * A game session
 * @param <GF> The playing field
 */
public interface Game<GF> {
    /**
     * Perform a potentially-illegal action
     * @param action The action to attempt to perform
     * @throws IllegalActionException If the attempted action is illegal
     */
    void performAction(Action<GF> action);

    /**
     * Check if the action can be performed
     * @param action The action to attempt
     * @return A legal action, or an illegal action + a reason for its illegality
     */
    ActionLegality canPerformAction(Action<GF> action);

    /**
     * Get the game field of this game
     * @return The game field
     */
    GF getGameField();

    /**
     * Check if this game is complete
     * @return True if the game is complete
     */
    boolean isComplete();
}
