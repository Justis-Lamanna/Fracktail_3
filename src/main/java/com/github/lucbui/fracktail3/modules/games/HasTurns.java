package com.github.lucbui.fracktail3.modules.games;

/**
 * Add-on which indicates a game has turns
 */
public interface HasTurns {
    /**
     * Advance the turn to the next player
     */
    void advanceTurn();
}
