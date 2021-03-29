package com.github.lucbui.fracktail3.modules.games;

/**
 * An action that can be performed for this game
 * @param <GF> The game field
 */
public interface Action<GF> {
    /**
     * Perform the action on a game
     * @param gameState The current state of the game
     */
    void performAction(Game<GF> gameState);
}