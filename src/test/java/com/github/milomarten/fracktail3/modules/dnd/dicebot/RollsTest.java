package com.github.milomarten.fracktail3.modules.dnd.dicebot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RollsTest {
    @Mock
    private Dice dice;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void oneDiceRoll() {
        when(dice.roll()).thenReturn(10);
        Rolls rolls = Rolls.rollDice(dice);
        assertEquals(1, rolls.getRolls().size());
        assertEquals("(10)", rolls.getRawExpression());
        assertEquals("(10)", rolls.getPrettyExpression());
    }

    @Test
    void multiDiceRoll() {
        when(dice.roll()).thenReturn(10).thenReturn(5);
        Rolls rolls = Rolls.rollDice(dice, 2);
        assertEquals(2, rolls.getRolls().size());
        assertEquals("(10+5)", rolls.getRawExpression());
        assertEquals("(10+5)", rolls.getPrettyExpression());
    }

    @Test
    void multiDiceRollKeepHighest() {
        when(dice.roll()).thenReturn(10).thenReturn(5);
        Rolls rolls = Rolls.rollDice(dice, 2, false, 1);
        assertEquals(2, rolls.getRolls().size());
        assertEquals("(10)", rolls.getRawExpression());
        assertEquals("(10+~~5~~)", rolls.getPrettyExpression());
    }

    @Test
    void multiDiceRollKeepLowest() {
        when(dice.roll()).thenReturn(10).thenReturn(5);
        Rolls rolls = Rolls.rollDice(dice, 2, true, 1);
        assertEquals(2, rolls.getRolls().size());
        assertEquals("(5)", rolls.getRawExpression());
        assertEquals("(~~10~~+5)", rolls.getPrettyExpression());
    }
}