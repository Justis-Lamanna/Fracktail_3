package com.github.lucbui.fracktail3.modules.games.checkers.rule;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.ActionLegality;
import com.github.lucbui.fracktail3.modules.games.Rule;
import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Piece;
import com.github.lucbui.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;

import java.util.Optional;

public class MoveDistanceRule implements Rule<Checkerboard> {
    @Override
    public ActionLegality isLegalMove(Action<Checkerboard> action, Checkerboard board) {
        if(action instanceof MoveAction) {
            MoveAction ma = (MoveAction) action;
            Optional<Position> start = board.getPositionOfPiece(ma.getPiece());
            if(start.isPresent()) {
                Position s = start.get();
                if(!s.isDiagonal(ma.getPosition())) {
                    return ActionLegality.illegal("Can only move diagonally");
                } else if(s.isDistanceFrom(ma.getPosition(), 1)) {
                    return ActionLegality.legal();
                } else if(s.isDistanceFrom(ma.getPosition(), 2)) {
                    Position between = s.middle(ma.getPosition());
                    Optional<Piece> betweenPiece = board.getPiece(between);
                    if(betweenPiece.isPresent()) {
                        return ActionLegality.test(betweenPiece.get().getColor() != ma.getPlayer(),
                                "Can only jump over opponent pieces");
                    } else {
                        return ActionLegality.illegal("Can only jump over pieces");
                    }
                } else {
                    return ActionLegality.illegal("Can only move one or two spaces");
                }
            } else {
                return ActionLegality.illegal("Piece does not exist");
            }
        }
        return ActionLegality.legal();
    }
}
