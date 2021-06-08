package com.github.milomarten.fracktail3.modules.games.checkers.rule;

import com.github.milomarten.fracktail3.modules.games.Action;
import com.github.milomarten.fracktail3.modules.games.ActionLegality;
import com.github.milomarten.fracktail3.modules.games.Rule;
import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.action.MoveAction;

public class CorrectPieceRule implements Rule<Checkerboard> {
    @Override
    public ActionLegality isLegalMove(Action<Checkerboard> action, Checkerboard board) {
        if(action instanceof MoveAction) {
            MoveAction ma = (MoveAction) action;
            return ActionLegality.test(ma.getPlayer() == ma.getPiece().getColor(), "Can only move pieces of your color");
        }
        return ActionLegality.legal();
    }
}
