package com.github.milomarten.fracktail3.modules.games;

import com.github.milomarten.fracktail3.modules.games.standard.field.Board;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;

public class TestBoard extends Board<TestPiece> {
    @Override
    public boolean isValidPosition(TestPiece piece, Position position) {
        return true;
    }
}
