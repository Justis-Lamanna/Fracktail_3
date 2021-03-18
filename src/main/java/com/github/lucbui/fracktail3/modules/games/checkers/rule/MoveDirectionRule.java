package com.github.lucbui.fracktail3.modules.games.checkers.rule;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.ActionLegality;
import com.github.lucbui.fracktail3.modules.games.Position;
import com.github.lucbui.fracktail3.modules.games.Rule;
import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Color;
import com.github.lucbui.fracktail3.modules.games.checkers.Type;
import com.github.lucbui.fracktail3.modules.games.checkers.action.MoveAction;

public class MoveDirectionRule implements Rule<Checkerboard> {
    @Override
    public ActionLegality isLegalMove(Action<Checkerboard> action, Checkerboard board) {
        if(action instanceof MoveAction) {
            MoveAction ma = (MoveAction) action;
            if(ma.getPiece().getType() == Type.MAN) {
                Position end = ma.getPosition();
                return board.getPositionOfPiece(ma.getPiece())
                        .map(start -> {
                            if(ma.getPiece().getColor() == Color.RED) {
                                return ActionLegality.test(end.isBelow(start), "Can only move red men down");
                            } else {
                                return ActionLegality.test(end.isAbove(start), "Can only move black men up");
                            }
                        })
                        .orElse(ActionLegality.illegal("Piece does not exist"));
            }
        }
        return ActionLegality.legal();
    }
}
