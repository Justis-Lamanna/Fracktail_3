package com.github.lucbui.fracktail3.modules.games.checkers;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Encapsulates a specific checker's piece
 */
@Data
@RequiredArgsConstructor
public class Piece {
    private final UUID uuid = UUID.randomUUID();
    private final Color color;
    private Type type = Type.MAN;

    public Piece(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    /**
     * King this piece
     */
    public void king() {
        this.type = Type.KING;
    }
}
