package com.github.lucbui.fracktail3.modules.games.standard.action;

import com.github.lucbui.fracktail3.modules.games.standard.field.TurnBasedGameField;
import lombok.Data;

@Data
public class AdvanceAction<PLAYER, GF extends TurnBasedGameField<PLAYER>> implements InTurnAction<PLAYER, GF> {
    private final PLAYER player;

    @Override
    public void performAction(GF gameState) {
        gameState.advanceTurn();
    }
}

