package com.github.lucbui.fracktail3.modules.games.standard.action;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.standard.field.TurnBasedGameField;

public interface InTurnAction<PLAYER, GF extends TurnBasedGameField<PLAYER>> extends Action<GF> {
    PLAYER getPlayer();
}