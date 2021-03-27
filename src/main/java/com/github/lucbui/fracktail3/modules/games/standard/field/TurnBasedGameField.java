package com.github.lucbui.fracktail3.modules.games.standard.field;

/**
 * Add-on which indicates a game has turns
 */
public interface TurnBasedGameField<PLAYER> extends PlayerGameField<PLAYER> {
    /**
     * Get the player currently in play
     * @return The player whose turn it currently is
     */
    PLAYER getCurrentPlayer();

    /**
     * Advance the turn to the next player
     */
    void advanceTurn();

    /**
     * Advance turn to a specific player
     * @param player The player to advance to
     */
    void advanceTurnTo(PLAYER player);
}
