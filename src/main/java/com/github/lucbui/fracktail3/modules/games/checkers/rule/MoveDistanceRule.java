package com.github.lucbui.fracktail3.modules.games.checkers.rule;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.ActionLegality;
import com.github.lucbui.fracktail3.modules.games.Rule;
import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Color;
import com.github.lucbui.fracktail3.modules.games.checkers.Piece;
import com.github.lucbui.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;

import java.util.Optional;

public class MoveDistanceRule implements Rule<Checkerboard> {
    @Override
    public ActionLegality isLegalMove(Action<Checkerboard> action, Checkerboard board) {
        if(action instanceof MoveAction) {
            MoveAction ma = (MoveAction) action;
            Optional<Position> startOpt = board.getPositionOfPiece(ma.getPiece());
            if(startOpt.isPresent()) {
                Position start = startOpt.get();
                Position next = ma.getPosition();
                if(ma.isMultiAction()) {
                    //To make everything easier, we perform all our actions on a copy of the board.
                    Checkerboard simulation = new Checkerboard(board);
                    ActionLegality legality = isLegalJump(simulation, ma.getPlayer(), start, next)
                            .doIfLegal(() -> performSimulatedJump(simulation, ma.getPlayer(), ma.getPiece(), next));
                    if(legality.isIllegal()) { return legality; }
                    Position current = next;
                    for(Position future : ma.getOtherPositions()) {
                        legality = isLegalJump(simulation, ma.getPlayer(), current, future)
                                .doIfLegal(() -> performSimulatedJump(simulation, ma.getPlayer(), ma.getPiece(), future));
                        if(legality.isIllegal()) { return legality; }
                        current = future;
                    }
                } else {
                    if(start.isDistanceFrom(next, 1)) {
                        return isDiagonal(start, next);
                    } else {
                        return isLegalJump(board, ma.getPlayer(), start, next);
                    }
                }
            } else {
                return ActionLegality.illegal("Piece does not exist");
            }
        }
        return ActionLegality.legal();
    }

    protected ActionLegality isLegalJump(Checkerboard board, Color player, Position start, Position next) {
        return isDiagonal(start, next).and(
                () -> isSingleJump(board, player, start, next));
    }

    protected void performSimulatedJump(Checkerboard board, Color player, Piece piece, Position next) {
        MoveAction ma = new MoveAction(player, piece, next);
        ma.performAction(board);
    }

    protected ActionLegality isDiagonal(Position start, Position next) {
        return ActionLegality.test(start.isDiagonal(next), "Can only move diagonally");
    }

    protected ActionLegality isSingleJump(Checkerboard board, Color player, Position start, Position next) {
        if(start.isDistanceFrom(next, 2)) {
            Position between = start.middle(next);
            Optional<Piece> betweenPiece = board.getPiece(between);
            if(betweenPiece.isPresent()) {
                return ActionLegality.test(betweenPiece.get().getColor() != player,
                        "Can only jump over opponent pieces");
            } else {
                return ActionLegality.illegal("Can only jump over pieces");
            }
        } else {
            return ActionLegality.illegal("Illegal jump");
        }
    }
}
