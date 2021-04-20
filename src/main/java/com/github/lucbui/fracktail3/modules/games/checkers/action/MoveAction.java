package com.github.lucbui.fracktail3.modules.games.checkers.action;

import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Color;
import com.github.lucbui.fracktail3.modules.games.checkers.Piece;
import com.github.lucbui.fracktail3.modules.games.standard.action.IMoveAction;
import com.github.lucbui.fracktail3.modules.games.standard.action.InTurnAction;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * An action which moves a checkers piece to one or more positions
 */
@Data
@RequiredArgsConstructor
public class MoveAction implements
        InTurnAction<Checkerboard, Color>,
        IMoveAction<Checkerboard, Piece> {
    private final Color player;
    private final Piece piece;
    private final Position position;
    private final Position[] otherPositions;

    //For the standard one-move action
    public MoveAction(Color player, Piece piece, Position position) {
        this(player, piece, position, null);
    }

    @Override
    public void performAction(Checkerboard field) {
        field.getPositionOfPiece(piece)
                .ifPresent(start -> {
                    doMove(field, start, position);
                    if(isMultiAction()) {
                        Position current = position;
                        for(Position next : otherPositions) {
                            doMove(field, current, next);
                            current = next;
                        }
                    }

                    field.advanceTurn();
                });
    }

    public boolean isMultiAction() {
        return otherPositions != null && otherPositions.length > 0;
    }

    protected void doMove(Checkerboard field, Position start, Position end) {
        field.movePiece(piece, start, end);
        //Jump the middle piece
        if(isJump(start, end)) {
            List<Position> check = start.between(end);
            check.forEach(field::removePieces);
        }
        //Promote
        if(isPromote(field, end)) {
            piece.king();
        }
    }

    protected boolean isJump(Position start, Position end) {
        return start.isDistanceFrom(end, 2);
    }

    protected boolean isPromote(Checkerboard field, Position end) {
        return piece.getColor().getPromoteRow().apply(field) == end.getRow();
    }
}
