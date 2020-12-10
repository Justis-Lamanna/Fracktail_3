package com.github.lucbui.fracktail3.dnd;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTest {
    @Test
    void testIdentityConversion() {
        assertEquals(new BigDecimal("1.000"), Money.COPPER.convertTo(Money.COPPER));
    }

    @Test
    void testUpwardConversion() {
        assertEquals(new BigDecimal("100.000"), Money.GOLD.convertTo(Money.COPPER));
    }

    @Test
    void testDownwardConversion() {
        assertEquals(new BigDecimal("0.010"), Money.COPPER.convertTo(Money.GOLD));
    }
}