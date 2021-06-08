package com.github.milomarten.fracktail3.modules.games.standard.action;

import com.github.milomarten.fracktail3.modules.games.Action;
import com.github.milomarten.fracktail3.modules.games.standard.field.TurnBasedGameField;

public interface InTurnAction<GF extends TurnBasedGameField<PLAYER>, PLAYER> extends Action<GF> {
    PLAYER getPlayer();
}
