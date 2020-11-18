package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import groovy.util.Eval;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class DiceBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiceBot.class);

    //Pattern which provides the following:
    //Group 1 - Number of Dice
    //Group 2 - Dice to roll
    //Group 3 - l, if lowest, or empty, if highest
    //Group 4 - Keep amount
    private final Pattern DICE_ROLL_PATTERN = Pattern.compile("([0-9]+)?d([0-9]+)(?>k(l)?([0-9]+))?");
    private final Pattern NUMBERS_PATTERN = Pattern.compile("[0-9]+");
    private final Pattern OPERATIONS_PATTERN = Pattern.compile("([+\\-*/()])");
    private final Pattern LESS_THAN_OR_EQUALS = Pattern.compile("(≤|⩽|<=)");
    private final Pattern GREATER_THAN_OR_EQUALS = Pattern.compile("(≥|⩾|>=)");
    private final Pattern NOT_EQUALS = Pattern.compile("(≠|<>|=/=)");

    private final Pattern COMPARISON_PATTERN =
            Pattern.compile("(" +
                    LESS_THAN_OR_EQUALS.pattern() + "|" +
                    GREATER_THAN_OR_EQUALS.pattern() + "|" +
                    "<+|>+|==|" +
                    NOT_EQUALS.pattern() + "!=)");

    private final Pattern VALID_EXPRESSION_REGEX =
            Pattern.compile("(?>" +
                    DICE_ROLL_PATTERN.pattern() + "|" +
                    NUMBERS_PATTERN.pattern() + "|" +
                    OPERATIONS_PATTERN.pattern() + "|" +
                    COMPARISON_PATTERN.pattern() + ")+");


    public RollResult roll(String e) {
        String expression = normalizeExpression(e);
        validateExpression(expression);

        Matcher matcher = DICE_ROLL_PATTERN.matcher(expression);
        Matcher matcher2 = DICE_ROLL_PATTERN.matcher(expression);
        StringBuffer rawExpr = new StringBuffer();
        StringBuffer prettyExpr = new StringBuffer();
        while(matcher.find() && matcher2.find()) {
            int diceFace = parse(matcher.group(2));
            int numDice = parseOpt(matcher.group(1)).orElse(1);
            boolean lowest = StringUtils.equals(matcher.group(3), "l");
            int keep = parseOpt(matcher.group(4)).orElse(numDice);
            Rolls result = Rolls.rollDice(new Dice(diceFace), numDice, lowest, keep);
            matcher.appendReplacement(rawExpr, result.getRawExpression());
            matcher2.appendReplacement(prettyExpr, result.getPrettyExpression());
        }
        matcher.appendTail(rawExpr);
        matcher2.appendTail(prettyExpr);

        try {
            Object o = Eval.me(rawExpr.toString());
            return new RollResult(o, denormalizeExpression(prettyExpr.toString()));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Bad Expression", ex);
        }
    }

    protected String normalizeExpression(String expression) {
        expression = StringUtils.deleteWhitespace(expression);
        expression = LESS_THAN_OR_EQUALS.matcher(expression).replaceAll("<=");
        expression = GREATER_THAN_OR_EQUALS.matcher(expression).replaceAll(">=");
        expression = NOT_EQUALS.matcher(expression).replaceAll("!=");
        return expression;
    }

    protected void validateExpression(String expression) {
        if(!VALID_EXPRESSION_REGEX.matcher(expression).matches()) {
            throw new IllegalArgumentException("Bad Expression");
        }
    }

    protected String denormalizeExpression(String expression) {
        //Nice formatting. Spaces around every operator, fancy ASCII
        expression = OPERATIONS_PATTERN.matcher(expression).replaceAll(" $1 ");
        expression = COMPARISON_PATTERN.matcher(expression).replaceAll(" $1 ");
        expression = LESS_THAN_OR_EQUALS.matcher(expression).replaceAll("≤");
        expression = GREATER_THAN_OR_EQUALS.matcher(expression).replaceAll("≥");
        expression = NOT_EQUALS.matcher(expression).replaceAll("≠");
        return expression;
    }

    @Command
    public Mono<Void> roll(CommandUseContext<?> ctx) {
        try {
            RollResult result = roll(ctx.getRawParameters());
            return ctx.respond(result.getPrettyExpression() + " ⟶ " + FormatUtils.bold(result.getResultStr()));
        } catch (IllegalArgumentException ex) {
            LOGGER.info("Exception in roll command", ex);
            return ctx.respond("Something went wrong evaluating that. Check your syntax!");
        }
    }

    private OptionalInt parseOpt(String s) {
        if(s == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(Integer.parseInt(s));
    }

    private int parse(String s) {
        if(s == null) {
            throw new NumberFormatException("Number cannot be blank");
        }
        return Integer.parseInt(s);
    }

    private static class Dice {
        private static final Random random = new Random();
        private final int faces;

        private Dice(int faces) {
            this.faces = faces;
        }

        public int roll() {
            return random.nextInt(faces) + 1;
        }
    }

    private static class Rolls {
        private final List<Integer> rolls;
        private final int keep;
        private final boolean lowest;

        public Rolls(List<Integer> rolls, int keep, boolean lowest) {
            this.rolls = rolls;
            this.keep = keep;
            this.lowest = lowest;
        }

        public static Rolls rollDice(Dice dice, int numberOfRolls, boolean lowest, int keep) {
            List<Integer> rolls = IntStream.range(0, numberOfRolls)
                    .map(x -> dice.roll())
                    .boxed()
                    .collect(Collectors.toList());
            return new Rolls(rolls, keep, lowest);
        }

        private Stream<Integer> getRolls() {
            Stream<Integer> rollStr = rolls.stream();
            if(keep > 0) {
                if(lowest) {
                    rollStr = rollStr.sorted();
                } else {
                    rollStr = rollStr.sorted(Comparator.reverseOrder());
                }
                rollStr = rollStr.limit(keep);
            }
            return rollStr;
        }

        public String getRawExpression() {
            return getRolls().map(i -> Integer.toString(i))
                    .collect(Collectors.joining("+", "(", ")"));
        }

        public String getPrettyExpression() {
            if(keep > 0) {
                Multiset<Integer> keptRolls = getRolls().collect(Collectors.toCollection(HashMultiset::create));
                StringJoiner sb = new StringJoiner("+", "(", ")");
                for(int roll : rolls) {
                    if(keptRolls.remove(roll)) {
                        sb.add(Integer.toString(roll));
                    } else {
                        sb.add(FormatUtils.strikethrough(Integer.toString(roll)));
                    }
                }
                assert keptRolls.isEmpty();
                return sb.toString();
            } else {
                return getRolls()
                        .map(i -> Integer.toString(i))
                        .collect(Collectors.joining("+", "(", ")"));
            }
        }
    }

    public static class RollResult {
        private final Object result;
        private final String prettyExpression;

        public RollResult(Object result, String prettyExpression) {
            this.result = result;
            this.prettyExpression = prettyExpression;
        }

        public String getResultStr() {
            if(result instanceof BigDecimal) {
                return ((BigDecimal)result).setScale(2, RoundingMode.FLOOR).toString();
            } else if(result instanceof Boolean) {
                return StringUtils.capitalize(BooleanUtils.toStringTrueFalse((Boolean) result));
            } else {
                return Objects.toString(result);
            }
        }

        public String getPrettyExpression() {
            return prettyExpression;
        }
    }
}
