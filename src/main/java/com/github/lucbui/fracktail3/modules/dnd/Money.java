package com.github.lucbui.fracktail3.modules.dnd;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;

/**
 * Represents money in DnD
 */
public enum Money {
    /**
     * One copper, the basic monetary unit
     */
    COPPER(1, "C"),
    /**
     * One silver, which represents 10 copper
     */
    SILVER(10, "S"),
    /**
     * One electrum, which represents 5 silver, or 50 copper
     */
    ELECTRUM(50, "E"),
    /**
     * One gold, which represents 10 silver or 100 copper
     */
    GOLD(100, "G"),
    /**
     * One platinum, which represents 10 gold, 100 silver, or 1,000 copper
     */
    PLATINUM(1_000, "P");

    private final BigDecimal numCopper;
    private final String unit;

    Money(int numCopper, String unit) {
        this.numCopper = BigDecimal.valueOf(numCopper);
        this.unit = unit;
    }

    /**
     * Get the number of copper pieces this money represents
     * @return The number of copper pieces this money represents
     */
    public BigDecimal getNumCopper() {
        return numCopper;
    }

    /**
     * Get the unit name
     * @return Unit name
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Get the conversion rate between this and another money type
     * @param to The unit type
     * @return The conversion rate between the two moneys
     */
    public BigDecimal convertTo(Money to) {
        if(this == to) {
            return this.numCopper;
        }
        return this.numCopper.divide(to.numCopper, 3, RoundingMode.DOWN);
    }

    /**
     * Get a money from the unit name (C, S, G, E, or P)
     * @param unit The unit
     * @return The Money, or Optional if no match
     */
    public static Optional<Money> valueFromUnit(String unit) {
        return Arrays.stream(Money.values())
                .filter(m -> m.unit.equals(unit))
                .findFirst();
    }
}
