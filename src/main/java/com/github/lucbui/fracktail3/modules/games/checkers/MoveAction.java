package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.Game;
import com.github.lucbui.fracktail3.modules.games.Position;
import lombok.Data;

import java.util.List;

/**
 * An action which moves a checkers piece to one or more positions
 */
@Data
public class MoveAction implements Action<Checkerboard<Piece>> {
    private final Piece piece;
    private final List<Position> positions;

    public MoveAction(Piece piece, List<Position> positions) {
        if(positions.size() < 1) {
            throw new IllegalArgumentException("Must move one or more positions");
        }
        this.piece = piece;
        this.positions = positions;
    }

    @Override
    public void performAction(Game<Checkerboard<Piece>> gameState) {

    }
}
