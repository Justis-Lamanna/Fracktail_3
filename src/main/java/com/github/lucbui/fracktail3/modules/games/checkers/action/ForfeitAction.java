package com.github.lucbui.fracktail3.modules.games.checkers.action;

import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Color;
import com.github.lucbui.fracktail3.modules.games.standard.action.InTurnAction;
import lombok.Data;

@Data
public class ForfeitAction implements InTurnAction<Checkerboard, Color> {
    private final Color player;

    @Override
    public void performAction(Checkerboard gameState) {
        gameState.setForfeitedPlayer(player);
    }
}
