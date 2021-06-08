package com.github.milomarten.fracktail3.modules.games.standard.field;

import java.util.Collection;

public interface PlayerGameField<PLAYER> {
    /**
     * Get all players in the game
     * @return The players of the game
     */
    Collection<PLAYER> getAllPlayers();

    /**
     * Get the number of players in the game
     * @return The number of players
     */
    default int getNumberOfPlayers() {
        return getAllPlayers().size();
    }
}
