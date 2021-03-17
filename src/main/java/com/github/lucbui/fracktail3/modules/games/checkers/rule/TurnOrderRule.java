package com.github.lucbui.fracktail3.modules.games.checkers.rule;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.ActionLegality;
import com.github.lucbui.fracktail3.modules.games.Rule;
import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.action.InTurnAction;

public class TurnOrderRule implements Rule<Checkerboard> {
    @Override
    public ActionLegality isLegalMove(Action<Checkerboard> action, Checkerboard board) {
        if(action instanceof InTurnAction) {
            InTurnAction ita = (InTurnAction) action;
            return ActionLegality.test(ita.getPlayerNum() == board.getCurrentPlayer(), "It is not this player's turn");
        }
        return ActionLegality.legal();
    }
}
