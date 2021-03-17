package com.github.lucbui.fracktail3.modules.games.checkers.action;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.Game;
import com.github.lucbui.fracktail3.modules.games.Position;
import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Piece;
import lombok.Data;

/**
 * An action which moves a checkers piece to one or more positions
 */
@Data
public class MoveAction implements Action<Checkerboard>, InTurnAction {
    private final int playerNum;
    private final Piece piece;
    private final Position position;

    @Override
    public void performAction(Game<Checkerboard> gameState) {
        Checkerboard field = gameState.getGameField();
        field.getPositionOfPiece(piece)
                .ifPresent(start -> {
                    field.movePiece(piece, start, position);
                    //Jump the middle piece
                    Position check = start.interpolate(position, 0.5);
                    field.removePieces(check);
                });
    }
}
