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

public class MoveDirectionRule implements Rule<Checkerboard> {
    @Override
    public ActionLegality isLegalMove(Action<Checkerboard> action, Checkerboard board) {
        if(action instanceof MoveAction) {
            MoveAction ma = (MoveAction) action;
            Piece piece = ma.getPiece();
            Position end = ma.getPosition();
            if(ma.isMultiAction()) {
                Checkerboard simulation = new Checkerboard(board);
                return simulation.getPositionOfPiece(piece)
                        .map(start -> {
                            ActionLegality legality = isMovingTheRightDirection(piece, start, end)
                                    .doIfLegal(() -> performSimulatedKing(simulation, ma.getPlayer(), piece, end));
                            if(legality.isIllegal()) return legality;
                            Position current = end;
                            for(Position future : ma.getOtherPositions()) {
                                legality = isMovingTheRightDirection(piece, current, future)
                                        .doIfLegal(() -> performSimulatedKing(simulation, ma.getPlayer(), piece, future));
                                if(legality.isIllegal()) return legality;
                                current = future;
                            }
                            return ActionLegality.legal();
                        })
                        .orElse(ActionLegality.illegal("Piece does not exist"));
            } else {
                return board.getPositionOfPiece(piece)
                        .map(start -> isMovingTheRightDirection(piece, start, end))
                        .orElse(ActionLegality.illegal("Piece does not exist"));
            }
        }
        return ActionLegality.legal();
    }

    protected ActionLegality isMovingTheRightDirection(Piece piece, Position start, Position end) {
        if(piece.getType() == Type.KING) {
            return ActionLegality.legal();
        } else if(piece.getColor() == Color.RED) {
            return ActionLegality.test(end.isBelow(start), "Can only move red men down");
        } else {
            return ActionLegality.test(end.isAbove(start), "Can only move black men up");
        }
    }

    protected void performSimulatedKing(Checkerboard board, Color player, Piece piece, Position next) {
        MoveAction ma = new MoveAction(player, piece, next);
        ma.performAction(board);
    }
}
