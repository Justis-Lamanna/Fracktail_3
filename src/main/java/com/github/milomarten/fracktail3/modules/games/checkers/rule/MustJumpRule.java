package com.github.milomarten.fracktail3.modules.games.checkers.rule;

import com.github.milomarten.fracktail3.modules.games.Action;
import com.github.milomarten.fracktail3.modules.games.ActionLegality;
import com.github.milomarten.fracktail3.modules.games.Rule;
import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.Color;
import com.github.milomarten.fracktail3.modules.games.checkers.Piece;
import com.github.milomarten.fracktail3.modules.games.checkers.Type;
import com.github.milomarten.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;

import java.util.Optional;

public class MustJumpRule implements Rule<Checkerboard> {
    @Override
    public ActionLegality isLegalMove(Action<Checkerboard> action, Checkerboard board) {
        if(action instanceof MoveAction) {
            MoveAction ma = (MoveAction) action;
            if(!ma.isMultiAction()) {
                return board.getPositionOfPiece(ma.getPiece())
                        .map(start -> {
                            if(start.isDistanceFrom(ma.getPosition(), 2)) {
                                return ActionLegality.legal(); //Already trying to jump, so we don't need to verify
                            }
                            return checkForNearbyJumps(board, ma.getPiece(), ma.getPlayer(), start);
                        })
                        .orElse(ActionLegality.illegal("Piece does not exist"));
            }
        }
        return ActionLegality.legal();
    }

    protected ActionLegality checkForNearbyJumps(Checkerboard board, Piece piece, Color player, Position start) {
        if(piece.getType() == Type.MAN) {
            if(piece.getColor() == Color.RED) {
                return checkPositionsForPieces(board, player,
                        start.add(1, -1), start.add(1, 1));
            } else {
                return checkPositionsForPieces(board, player,
                        start.add(-1, -1), start.add(-1, 1));
            }
        } else {
            return checkPositionsForPieces(board, player,
                    start.add(1, -1), start.add(1, 1),
                    start.add(-1, -1), start.add(-1, 1));
        }
    }

    protected ActionLegality checkPositionsForPieces(Checkerboard board, Color player, Position... positions) {
        for(Position position : positions) {
            Optional<Piece> piece = board.getPiece(position);
            if(piece.isPresent() && piece.get().getColor() != player) {
                return ActionLegality.illegal("Attempting to move without capturing an opponent");
            }
        }
        return ActionLegality.legal();
    }
}
