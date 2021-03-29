package com.github.lucbui.fracktail3.modules.games.standard.action;

import com.github.lucbui.fracktail3.modules.games.BasicGame;
import com.github.lucbui.fracktail3.modules.games.Game;
import com.github.lucbui.fracktail3.modules.games.TestPiece;
import com.github.lucbui.fracktail3.modules.games.standard.field.TurnBasedGameField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class AdvanceActionTest {
    @Mock
    private TurnBasedGameField<TestPiece> gameField;

    private Game<TurnBasedGameField<TestPiece>> testGame;

    @BeforeEach
    public void initializeGame() {
        testGame = new BasicGame<>(Collections.emptyList(), gameField);
    }

    @Test
    public void testAdvancesTurnOnAction() {
        AdvanceAction<TestPiece, TurnBasedGameField<TestPiece>> action = new AdvanceAction<>(TestPiece.INSTANCE);
        testGame.performAction(action);

        Mockito.verify(gameField).advanceTurn();
    }
}