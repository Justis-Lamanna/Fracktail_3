package com.github.lucbui.fracktail3.dnd.moneybot;

import com.github.lucbui.fracktail3.dnd.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
        String str = moneyBot.getFriendlyString(MoneyAmount.of(5, Money.COPPER), MoneyBot.STANDARD_MONEY);
        assertEquals("5CP", str);
    }

    @Test
    void testGetFriendlyStringExcessCopper() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(20, Money.COPPER), MoneyBot.STANDARD_MONEY);
        assertEquals("2SP", str);
    }

    @Test
    void testGetFriendlyStringSilver() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(5, Money.SILVER), MoneyBot.STANDARD_MONEY);
        assertEquals("5SP", str);
    }

    @Test
    void testGetFriendlyStringExcessSilver() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(20, Money.SILVER), MoneyBot.STANDARD_MONEY);
        assertEquals("2GP", str);
    }

    @Test
    void testGetFriendlyStringGold() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(5, Money.GOLD), MoneyBot.STANDARD_MONEY);
        assertEquals("5GP", str);
    }

    @Test
    void testGetFriendlyStringExcessGold() {
        String str = moneyBot.getFriendlyString(MoneyAmount.of(20, Money.GOLD), MoneyBot.STANDARD_MONEY);
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

    @Test
    void testParseSimple() {
        MoneyAmount amt = moneyBot.parse("5CP").getAmount();
        assertEquals(BigDecimal.valueOf(5), amt.inUnits(Money.COPPER));
    }

    @Test
    void testParseComplex() {
        MoneyAmount amt = moneyBot.parse("5CP10SP3GP").getAmount();
        assertEquals(BigDecimal.valueOf(405), amt.inUnits(Money.COPPER));
    }

    @Test
    void testParseComplexCoinPurse() {
        Set<Money> coinset = moneyBot.parse("5CP10SP3GP").getCoins();
        assertTrue(coinset.contains(Money.COPPER));
        assertTrue(coinset.contains(Money.SILVER));
        assertTrue(coinset.contains(Money.GOLD));
        assertEquals(3, coinset.size());
    }

    @Test
    void testParseInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            moneyBot.parse("xxx");
        });
    }

    @Test
    void testSplitCommandSimpleClean() {
        String response = moneyBot.split("5CP", 5);
        assertEquals("To split that, each person gets 1CP.", response);
    }

    @Test
    void testSplitCommandSimpleUnclean() {
        String response = moneyBot.split("5CP", 4);
        assertEquals("To split that, each person gets 1CP, with 1CP left over.", response);
    }

    @Test
    void testSplitCommandNotEnough() {
        String response = moneyBot.split("4CP", 5);
        assertEquals("To split that, 1 people get 1CP, and the rest get none.", response);
    }

    @Test
    void testSplitCommandNotPerfectAmountOfGoldEnough() {
        String response = moneyBot.split("4GP", 5);
        assertEquals("To split that, each person gets 8SP.", response);
    }

    @Test
    void testSplitCommandNotPerfectAmountWithUnusualCurrency() {
        String response = moneyBot.split("4EP", 5);
        assertEquals("To split that, each person gets 4SP.", response);
    }
}