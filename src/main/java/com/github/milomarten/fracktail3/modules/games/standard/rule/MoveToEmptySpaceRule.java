package com.github.milomarten.fracktail3.modules.games.standard.rule;

import com.github.milomarten.fracktail3.modules.games.Action;
import com.github.milomarten.fracktail3.modules.games.ActionLegality;
import com.github.milomarten.fracktail3.modules.games.Rule;
import com.github.milomarten.fracktail3.modules.games.standard.action.IMoveAction;
import com.github.milomarten.fracktail3.modules.games.standard.field.Board;

public class MoveToEmptySpaceRule<T extends Board<?>> implements Rule<T> {
    @Override
    public ActionLegality isLegalMove(Action<T> action, T board) {
        if(action instanceof IMoveAction) {
            IMoveAction<?, ?> ma = (IMoveAction<?, ?>) action;
            return ActionLegality.test(board.getPieces(ma.getPosition()).isEmpty(), "Can only move to an empty spot");
        }
        return ActionLegality.legal();
    }
}
