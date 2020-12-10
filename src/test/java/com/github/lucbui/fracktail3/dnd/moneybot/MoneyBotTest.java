package com.github.lucbui.fracktail3.dnd.moneybot;

import com.github.lucbui.fracktail3.dnd.Money;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyBotTest {
    private final MoneyBot moneyBot = new MoneyBot();

    @Test
    void testEvenSplit() {
        MoneySplit split = moneyBot.split(MoneyAmount.of(4, Money.COPPER), 4);
        assertEquals(MoneyAmount.ONE_COPPER, split.getForEach());
        assertEquals(MoneyAmount.ZERO, split.getLeftover());
    }

    @Test
    void testUnevenSplit() {
        MoneySplit split = moneyBot.split(MoneyAmount.of(5, Money.COPPER), 4);
        assertEquals(MoneyAmount.ONE_COPPER, split.getForEach());
        assertEquals(MoneyAmount.ONE_COPPER, split.getLeftover());
    }

    @Test
    void testGetFriendlyStringCopper() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(5, Money.COPPER));
        assertEquals("5CP", str);
    }

    @Test
    void testGetFriendlyStringExcessCopper() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(20, Money.COPPER));
        assertEquals("2SP", str);
    }

    @Test
    void testGetFriendlyStringSilver() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(5, Money.SILVER));
        assertEquals("5SP", str);
    }

    @Test
    void testGetFriendlyStringExcessSilver() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(20, Money.SILVER));
        assertEquals("2GP", str);
    }

    @Test
    void testGetFriendlyStringGold() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(5, Money.GOLD));
        assertEquals("5GP", str);
    }

    @Test
    void testGetFriendlyStringExcessGold() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(20, Money.GOLD));
        assertEquals("20GP", str);
    }

    @Test
    void testGetFriendlyStringExcessCopperAllowElectrum() {
        Set<Money> set = EnumSet.of(Money.COPPER, Money.SILVER, Money.ELECTRUM, Money.GOLD);
        String str = moneyBot.getFriendlyString(MoneyAmount.of(50, Money.COPPER), set);
        assertEquals("1EP", str);
    }

    @Test
    void testGetFriendlyStringExcessGoldAllowPlatinum() {
        Set<Money> set = EnumSet.of(Money.COPPER, Money.SILVER, Money.GOLD, Money.PLATINUM);
        String str = moneyBot.getFriendlyString(MoneyAmount.of(20, Money.GOLD), set);
        assertEquals("2PP", str);
    }
}