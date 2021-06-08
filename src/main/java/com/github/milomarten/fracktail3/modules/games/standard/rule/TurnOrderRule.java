package com.github.milomarten.fracktail3.modules.games.standard.rule;

import com.github.milomarten.fracktail3.modules.games.Action;
import com.github.milomarten.fracktail3.modules.games.ActionLegality;
import com.github.milomarten.fracktail3.modules.games.Rule;
import com.github.milomarten.fracktail3.modules.games.standard.action.InTurnAction;
import com.github.milomarten.fracktail3.modules.games.standard.field.TurnBasedGameField;

public class TurnOrderRule<GF extends TurnBasedGameField<?>> implements Rule<GF> {
    @Override
    public ActionLegality isLegalMove(Action<GF> action, GF board) {
        if(action instanceof InTurnAction) {
            InTurnAction<?, ?> ia = (InTurnAction<?, ?>) action;
            return ActionLegality.test(board.getCurrentPlayer().equals(ia.getPlayer()), "Not that player's turn");
        }
        return ActionLegality.legal();
    }
}
