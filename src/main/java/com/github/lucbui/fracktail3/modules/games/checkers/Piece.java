package com.github.lucbui.fracktail3.modules.games.checkers;

import lombok.Data;

import java.util.UUID;

@Data
public class Piece {
    private final UUID uuid = UUID.randomUUID();
    private final Color color;
    private Type type = Type.MAN;

    public void king() {
        this.type = Type.KING;
    }
}
