package com.github.lucbui.fracktail3.modules.games.checkers.action;

import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Color;
import com.github.lucbui.fracktail3.modules.games.checkers.Piece;
import com.github.lucbui.fracktail3.modules.games.standard.action.IMoveAction;
import com.github.lucbui.fracktail3.modules.games.standard.action.InTurnAction;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import lombok.Data;

import java.util.List;

/**
 * An action which moves a checkers piece to one or more positions
 */
@Data
public class MoveAction implements
        InTurnAction<Checkerboard, Color>,
        IMoveAction<Checkerboard, Piece> {
    private final Color player;
    private final Piece piece;
    private final Position position;

    @Override
    public void performAction(Checkerboard field) {
        field.getPositionOfPiece(piece)
                .ifPresent(start -> {
                    field.movePiece(piece, start, position);
                    //Jump the middle piece
                    if(isJump(start)) {
                        List<Position> check = start.between(position);
                        check.forEach(field::removePieces);
                    }
                    //Promote
                    if(isPromote(field)) {
                        piece.king();
                    }
                });
    }

    protected boolean isJump(Position start) {
        return start.isDistanceFrom(position, 2);
    }

    protected boolean isPromote(Checkerboard field) {
        return piece.getColor().getPromoteRow().apply(field) == position.getRow();
    }
}
