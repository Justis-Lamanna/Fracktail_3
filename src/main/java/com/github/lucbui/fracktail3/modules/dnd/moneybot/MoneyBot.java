package com.github.lucbui.fracktail3.modules.dnd.moneybot;

import com.github.lucbui.fracktail3.modules.dnd.Money;
import com.github.lucbui.fracktail3.spring.command.annotation.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MoneyBot {
    public static final Set<Money> STANDARD_MONEY = EnumSet.of(Money.COPPER, Money.SILVER, Money.GOLD);

    private static final Pattern MONEY_VALIDATION_PATTERN = Pattern.compile("^(?>[0-9]+[CSEGP]P?)+$");
    private static final Pattern MONEY_PARSING_PATTERN = Pattern.compile("([0-9]+)([CSEGP])P?");
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
                    sb.append(divisionAndRemainder[0].stripTrailingZeros().toPlainString()).append(unit.getUnit()).append("P");
                }
                left = divisionAndRemainder[1];
            }
        }
        return sb.toString();
    }

    public MoneyParseResult parse(String moneyStr) {
        String normalized = StringUtils.upperCase(moneyStr);
        if(!MONEY_VALIDATION_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid format");
        }

        Matcher matcher = MONEY_PARSING_PATTERN.matcher(normalized);

        MoneyAmount total = MoneyAmount.ZERO;
        Set<Money> typesOfCoins = EnumSet.copyOf(STANDARD_MONEY);
        while(matcher.find()) {
            int amount = Integer.parseInt(matcher.group(1));
            Money coinage = Money.valueFromUnit(matcher.group(2)).orElseThrow(NoSuchElementException::new);
            typesOfCoins.add(coinage);
            total = total.plus(MoneyAmount.of(amount, coinage));
        }
        return new MoneyParseResult(total, typesOfCoins);
    }

    @Command
    @Usage("Split some Dungeons & Dragons, or Pathfinder, money amongst your party.\n" +
            "The format is !split <money> <party size>. The money format is <number><CP|SP|EP|GP|PP>, and can be repeated without spaces (example: 5CP3SP1GP)")
    @OnExceptionRespond(exception = IllegalArgumentException.class, value = @FString("Something went wrong evaluating that. Check your syntax!"))
    public String split(@Parameter(0) String expression, @Parameter(1) int split) {
        MoneyParseResult parseResult = parse(expression);
        MoneySplit splitResult = split(parseResult.amount, split);
        if(splitResult.getForEach().isZero()) {
            // Semi-edge case where you don't have enough money for everyone.
            // This only happens when you have less than <split> CP.
            int peopleGettingMoney = split - splitResult.getLeftover().inUnits(Money.COPPER).intValue();
            return "To split that, " + peopleGettingMoney + " people get 1CP, and the rest get none.";
        }
        String splitStr = getFriendlyString(splitResult.getForEach(), parseResult.coins);
        if(splitResult.getLeftover().isZero()) {
            return "To split that, each person gets " + splitStr + ".";
        } else {
            String leftoverStr = getFriendlyString(splitResult.getLeftover(), parseResult.coins);
            return "To split that, each person gets " + splitStr + ", with " + leftoverStr + " left over.";
        }
    }

    public static class MoneyParseResult {
        private final MoneyAmount amount;
        private final Set<Money> coins;

        public MoneyParseResult(MoneyAmount amount, Set<Money> coins) {
            this.amount = amount;
            this.coins = coins;
        }

        public MoneyAmount getAmount() {
            return amount;
        }

        public Set<Money> getCoins() {
            return coins;
        }
    }
}
