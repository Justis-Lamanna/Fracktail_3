package com.github.lucbui.fracktail3.modules.games.standard.rule;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.ActionLegality;
import com.github.lucbui.fracktail3.modules.games.standard.action.InTurnAction;
import com.github.lucbui.fracktail3.modules.games.standard.field.TurnBasedGameField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnOrderRuleTest {
    private static final TurnOrderRule<TurnBasedGameField<Integer>> RULE = new TurnOrderRule<>();

    @Mock
    private InTurnAction<TurnBasedGameField<Integer>, Integer> action;

    @Mock
    private TurnBasedGameField<Integer> gameField;

    @Test
    public void legalIfPlayerIsCorrect() {
        when(action.getPlayer()).thenReturn(0);
        when(gameField.getCurrentPlayer()).thenReturn(0);

        ActionLegality legality = RULE.isLegalMove(action, gameField);
        assertTrue(legality.isLegal());
    }

    @Test
    public void illegalIfPlayerIsIncorrect() {
        when(action.getPlayer()).thenReturn(1);
        when(gameField.getCurrentPlayer()).thenReturn(0);

        ActionLegality legality = RULE.isLegalMove(action, gameField);
        assertFalse(legality.isLegal());
        assertNotNull(legality.getReason());
    }

    @Test
    public void legalIfNonTurnAction() {
        Action<TurnBasedGameField<Integer>> nonTurnAction = Mockito.mock(Action.class);

        ActionLegality legality = RULE.isLegalMove(nonTurnAction, gameField);
        assertTrue(legality.isLegal());
    }
}