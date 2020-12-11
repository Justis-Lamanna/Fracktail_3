package com.github.lucbui.fracktail3.modules.dnd.moneybot;

import com.github.lucbui.fracktail3.modules.dnd.Money;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an amount of money
 */
public class MoneyAmount implements Comparable<MoneyAmount> {
    private final long numberOfCopper;
    private static final Money BASE_UNIT = Money.COPPER;

    /**
     * An amount of money equal to being broke
     */
    public static final MoneyAmount ZERO = new MoneyAmount(0);
    /**
     * An amount of money equal to one copper
     */
    public static final MoneyAmount ONE_COPPER = new MoneyAmount(1);
    /**
     * An amount of money equal to one silver
     */
    public static final MoneyAmount ONE_SILVER = MoneyAmount.of(1, Money.SILVER);
    /**
     * An amount of money equal to one gold
     */
    public static final MoneyAmount ONE_GOLD = MoneyAmount.of(1, Money.GOLD);
    /**
     * An amount of money equal to one platinum
     */
    public static final MoneyAmount ONE_PLATINUM = MoneyAmount.of(1, Money.PLATINUM);

    private MoneyAmount(long numberOfCopper) {
        this.numberOfCopper = numberOfCopper;
    }

    /**
     * Create a MoneyAmount from a money and amount
     * @param amount The amount of money
     * @param unit The money unit
     * @return The created MoneyAmount
     */
    public static MoneyAmount of(long amount, Money unit) {
        long convertedAmount = unit.convertTo(BASE_UNIT)
                .multiply(BigDecimal.valueOf(amount))
                .longValue();
        return new MoneyAmount(convertedAmount);
    }

    /**
     * Create a MoneyAmount from a money and amount
     * @param amount The amount of money
     * @param unit The money unit
     * @return The created MoneyAmount
     */
    public static MoneyAmount of(BigDecimal amount, Money unit) {
        long convertedAmount = unit.convertTo(BASE_UNIT)
                .multiply(amount)
                .longValue();
        return new MoneyAmount(convertedAmount);
    }

    /**
     * Get the amount of money needed, in a different unit
     * @param otherUnit The unit to convert to
     * @return The amount of money to convert this amount into the provided units
     */
    public BigDecimal inUnits(Money otherUnit) {
        if(otherUnit == BASE_UNIT) {
            return BigDecimal.valueOf(numberOfCopper);
        }
        return BASE_UNIT.convertTo(otherUnit)
                .multiply(BigDecimal.valueOf(numberOfCopper));
    }

    /**
     * Add two money amounts together
     * @param amount The amount to add to
     * @return A new MoneyAmount containing the sum total of this and the other amount
     */
    public MoneyAmount plus(MoneyAmount amount) {
        return new MoneyAmount(this.numberOfCopper + amount.numberOfCopper);
    }

    /**
     * Subtract two money amounts together
     * @param amount The amount to subtract
     * @return A new MoneyAmount containing the difference after subtracting the other from this
     */
    public MoneyAmount minus(MoneyAmount amount) {
        return new MoneyAmount(this.numberOfCopper - amount.numberOfCopper);
    }

    /**
     * Check if this amount is negative
     * @return True, if this money amount is negative
     */
    public boolean isNegative() {
        return this.numberOfCopper < 0;
    }

    /**
     * Check if this amount if zero
     * @return True, if this money amount is zero
     */
    public boolean isZero() {
        return this.numberOfCopper == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyAmount that = (MoneyAmount) o;
        return numberOfCopper == that.numberOfCopper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfCopper);
    }

    @Override
    public int compareTo(MoneyAmount o) {
        return Long.compare(this.numberOfCopper, o.numberOfCopper);
    }
}
