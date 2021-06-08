package com.github.milomarten.fracktail3.modules.games.standard.action;

import com.github.milomarten.fracktail3.modules.games.standard.field.TurnBasedGameField;
import lombok.Data;

@Data
public class AdvanceAction<PLAYER, GF extends TurnBasedGameField<PLAYER>> implements InTurnAction<GF, PLAYER> {
    private final PLAYER player;

    @Override
    public void performAction(GF gameState) {
        gameState.advanceTurn();
    }
}

