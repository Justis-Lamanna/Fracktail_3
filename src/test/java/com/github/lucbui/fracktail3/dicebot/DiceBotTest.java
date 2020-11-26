package com.github.lucbui.fracktail3.dicebot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class DiceBotTest {
    @Mock
    private DiceFactory diceFactory;

    @Mock
    private Dice dice;

    @InjectMocks
    private DiceBot diceBot;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        when(diceFactory.createDice(anyInt())).thenReturn(dice);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void oneDiceRoll() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20");
        assertEquals(10, result.getResult());
        assertEquals("10", result.getResultStr());
        assertEquals("( 10 )", result.getPrettyExpression());
    }

    @Test
    void multiDiceRoll() {
        when(dice.roll()).thenReturn(10).thenReturn(5);
        DiceBot.RollResult result = diceBot.rollForExpression("2d20");
        assertEquals(15, result.getResult());
        assertEquals("15", result.getResultStr());
        assertEquals("( 10 + 5 )", result.getPrettyExpression());
    }

    @Test
    void multiDiceRollKeepHighest() {
        when(dice.roll()).thenReturn(10).thenReturn(5);
        DiceBot.RollResult result = diceBot.rollForExpression("2d20k1");
        assertEquals(10, result.getResult());
        assertEquals("10", result.getResultStr());
        assertEquals("( 10 + ~~5~~ )", result.getPrettyExpression());
    }

    @Test
    void multiDiceRollKeepLowest() {
        when(dice.roll()).thenReturn(10).thenReturn(5);
        DiceBot.RollResult result = diceBot.rollForExpression("2d20kl1");
        assertEquals(5, result.getResult());
        assertEquals("5", result.getResultStr());
        assertEquals("( ~~10~~ + 5 )", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollPlusConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 + 2");
        assertEquals(12, result.getResult());
        assertEquals("12", result.getResultStr());
        assertEquals("( 10 ) + 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollMinusConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 - 2");
        assertEquals(8, result.getResult());
        assertEquals("8", result.getResultStr());
        assertEquals("( 10 ) - 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollTimesConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 * 2");
        assertEquals(20, result.getResult());
        assertEquals("20", result.getResultStr());
        assertEquals("( 10 ) * 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollDividedByConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 / 2");
        assertEquals(new BigDecimal("5"), result.getResult());
        assertEquals("5.00", result.getResultStr());
        assertEquals("( 10 ) / 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollLessThanConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 < 2");
        assertEquals(false, result.getResult());
        assertEquals("False", result.getResultStr());
        assertEquals("( 10 ) < 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollGreaterThanConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 > 2");
        assertEquals(true, result.getResult());
        assertEquals("True", result.getResultStr());
        assertEquals("( 10 ) > 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollLessThanOrEqualToConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 <= 2");
        assertEquals(false, result.getResult());
        assertEquals("False", result.getResultStr());
        assertEquals("( 10 ) ≤ 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollGreaterThanOrEqualToConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 >= 2");
        assertEquals(true, result.getResult());
        assertEquals("True", result.getResultStr());
        assertEquals("( 10 ) ≥ 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollEqualsConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 == 2");
        assertEquals(false, result.getResult());
        assertEquals("False", result.getResultStr());
        assertEquals("( 10 ) == 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollDoesNotEqualConstant() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 != 2");
        assertEquals(true, result.getResult());
        assertEquals("True", result.getResultStr());
        assertEquals("( 10 ) ≠ 2", result.getPrettyExpression());
    }

    @Test
    void oneDiceRollPlusOneDiceRoll() {
        when(dice.roll()).thenReturn(10).thenReturn(5);
        DiceBot.RollResult result = diceBot.rollForExpression("d20 + d20");
        assertEquals(15, result.getResult());
        assertEquals("15", result.getResultStr());
        assertEquals("( 10 ) + ( 5 )", result.getPrettyExpression());
    }

    @Test
    void twoDiceRollPlusOneDiceRoll() {
        when(dice.roll()).thenReturn(10).thenReturn(5).thenReturn(2);
        DiceBot.RollResult result = diceBot.rollForExpression("2d20 + d20");
        assertEquals(17, result.getResult());
        assertEquals("17", result.getResultStr());
        assertEquals("( 10 + 5 ) + ( 2 )", result.getPrettyExpression());
    }

    @Test
    void twoDiceRollKeepHighestPlusOneDiceRoll() {
        when(dice.roll()).thenReturn(10).thenReturn(5).thenReturn(2);
        DiceBot.RollResult result = diceBot.rollForExpression("2d20k1 + d20");
        assertEquals(12, result.getResult());
        assertEquals("12", result.getResultStr());
        assertEquals("( 10 + ~~5~~ ) + ( 2 )", result.getPrettyExpression());
    }

    @Test
    void twoDiceRollKeepLowestPlusOneDiceRoll() {
        when(dice.roll()).thenReturn(10).thenReturn(5).thenReturn(2);
        DiceBot.RollResult result = diceBot.rollForExpression("2d20kl1 + d20");
        assertEquals(7, result.getResult());
        assertEquals("7", result.getResultStr());
        assertEquals("( ~~10~~ + 5 ) + ( 2 )", result.getPrettyExpression());
    }

    @Test
    void negativeDiceRoll() {
        when(dice.roll()).thenReturn(10);
        DiceBot.RollResult result = diceBot.rollForExpression("-d20");
        assertEquals(-10, result.getResult());
        assertEquals("-10", result.getResultStr());
        assertEquals("- ( 10 )", result.getPrettyExpression());
    }

    @Test
    void invalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            diceBot.rollForExpression("hello, world");
        });
    }

    @Test
    void expressionUsingPowers() {
        assertThrows(IllegalArgumentException.class, () -> {
            diceBot.rollForExpression("d20 ** 2");
        });
    }

    @Test
    void expressionUsingNegativeAmountOfFaces() {
        assertThrows(IllegalArgumentException.class, () -> {
            diceBot.rollForExpression("d-10");
        });
    }

    @Test
    void expressionUsingZeroFaces() {
        assertThrows(IllegalArgumentException.class, () -> {
            diceBot.rollForExpression("d0");
        });
    }

    @Test
    void expressionUsingZeroRolls() {
        assertThrows(IllegalArgumentException.class, () -> {
            diceBot.rollForExpression("0d20");
        });
    }

    @Test
    void expressionUsingZeroKeep() {
        assertThrows(IllegalArgumentException.class, () -> {
            diceBot.rollForExpression("d20k0");
        });
    }
}