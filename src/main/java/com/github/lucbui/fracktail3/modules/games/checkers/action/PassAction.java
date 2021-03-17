package com.github.lucbui.fracktail3.modules.games.checkers.action;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.Game;
import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import lombok.Data;

@Data
public class PassAction implements Action<Checkerboard>, InTurnAction {
    private final int playerNum;

    @Override
    public void performAction(Game<Checkerboard> gameState) {
        gameState.getGameField().advanceTurn();
    }
}
