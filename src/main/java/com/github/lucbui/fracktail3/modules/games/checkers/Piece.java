package com.github.lucbui.fracktail3.modules.games.checkers;

import lombok.Data;

import java.util.UUID;

/**
 * Encapsulates a specific checker's piece
 */
@Data
public class Piece {
    private final UUID uuid = UUID.randomUUID();
    private final Color color;
    private Type type = Type.MAN;

    /**
     * King this piece
     */
    public void king() {
        this.type = Type.KING;
    }
}
