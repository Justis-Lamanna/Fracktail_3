package com.github.lucbui.fracktail3.dnd.moneybot;

import com.github.lucbui.fracktail3.dnd.Money;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.ParameterRange;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

@Component
public class MoneyBot {
    private static final Money[] reverseMoney;

    static {
        reverseMoney = Money.values();
        ArrayUtils.reverse(reverseMoney);
    }

    public MoneySplit split(MoneyAmount amount, int number) {
        BigDecimal numberOfCopper = amount.inUnits(Money.COPPER);
        BigDecimal[] copperForEachAndRemainder = numberOfCopper.divideAndRemainder(BigDecimal.valueOf(number));
        return new MoneySplit(
                MoneyAmount.of(copperForEachAndRemainder[0], Money.COPPER),
                MoneyAmount.of(copperForEachAndRemainder[1], Money.COPPER)
        );
    }

    public String getFriendlyString(MoneyAmount amount, Set<Money> permissibleUnits) {
        StringBuilder sb = new StringBuilder();
        BigDecimal left = amount.inUnits(Money.COPPER);
        for(Money unit : reverseMoney) {
            if(permissibleUnits.contains(unit)) {
                BigDecimal[] divisionAndRemainder = left.divideAndRemainder(unit.convertTo(Money.COPPER));
                if (!divisionAndRemainder[0].stripTrailingZeros().equals(BigDecimal.ZERO)) {
                    sb.append(divisionAndRemainder[0].toPlainString()).append(unit.getUnit()).append("P");
                }
                left = divisionAndRemainder[1];
            }
        }
        return sb.toString();
    }

    public String getFriendlyString(MoneyAmount amount) {
        return getFriendlyString(amount, EnumSet.of(Money.COPPER, Money.SILVER, Money.GOLD));
    }

    @Command
    public String split(@ParameterRange String expression) {
        return "";
    }
}
