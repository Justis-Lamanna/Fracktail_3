package com.github.milomarten.fracktail3.modules.dnd.dicebot;

import com.github.milomarten.fracktail3.discord.util.FormatUtils;
import com.github.milomarten.fracktail3.spring.command.annotation.Command;
import com.github.milomarten.fracktail3.spring.command.annotation.OnExceptionRespond;
import com.github.milomarten.fracktail3.spring.command.annotation.Parameter;
import com.github.milomarten.fracktail3.spring.command.annotation.Usage;
import groovy.util.Eval;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DiceBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiceBot.class);

    @Autowired
    private DiceFactory diceFactory;

    private static final String[] FORBIDDEN_CONSTRUCTS = {"**", "++", "--", "<<", "<<<", ">>"};

    //Pattern which provides the following:
    //Group 1 - Number of Dice
    //Group 2 - Dice to roll
    //Group 3 - l, if lowest, or empty, if highest
    //Group 4 - Keep amount
    private final Pattern DICE_ROLL_PATTERN = Pattern.compile("(\\d+)?d(\\d+)(?>k(l)?(\\d+))?");
    private final Pattern NUMBERS_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");
    private final Pattern OPERATIONS_PATTERN = Pattern.compile("(\\+|-|\\*|×|/|÷|≤|⩽|<=|≥|⩾|>=|==|≠|<>|=/=|!=|<|>)");

    private final Pattern MULTIPLICATION = Pattern.compile("(\\*|×)");
    private final Pattern DIVISION = Pattern.compile("(/|÷)");
    private final Pattern LESS_THAN_OR_EQUALS = Pattern.compile("(≤|⩽|<=)");
    private final Pattern GREATER_THAN_OR_EQUALS = Pattern.compile("(≥|⩾|>=)");
    private final Pattern NOT_EQUALS = Pattern.compile("(≠|<>|=/=|!=)");

    private final Pattern VALID_EXPRESSION_REGEX =
            Pattern.compile("(?>" +
                    DICE_ROLL_PATTERN.pattern() + "|" +
                    NUMBERS_PATTERN.pattern() + "|" +
                    OPERATIONS_PATTERN.pattern() + "|\\(|\\))+");


    public RollResult rollForExpression(String e) {
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
            if(diceFace == 0 || numDice == 0 || keep == 0) {
                throw new IllegalArgumentException("Bad Expression");
            }
            Rolls result = Rolls.rollDice(diceFactory.createDice(diceFace), numDice, lowest, keep);
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
        expression = MULTIPLICATION.matcher(expression).replaceAll("*");
        expression = DIVISION.matcher(expression).replaceAll("/");
        return expression;
    }

    protected void validateExpression(String expression) {
        if(!VALID_EXPRESSION_REGEX.matcher(expression).matches() |
                StringUtils.containsAny(expression, FORBIDDEN_CONSTRUCTS)) {
            throw new IllegalArgumentException("Bad Expression");
        }
    }

    protected String denormalizeExpression(String expression) {
        //Nice formatting. Spaces around every operator, fancy ASCII
        expression = OPERATIONS_PATTERN.matcher(expression).replaceAll(" $1 ");
        expression = LESS_THAN_OR_EQUALS.matcher(expression).replaceAll("≤");
        expression = GREATER_THAN_OR_EQUALS.matcher(expression).replaceAll("≥");
        expression = NOT_EQUALS.matcher(expression).replaceAll("≠");
        expression = expression.replaceAll("\\(", "( ");
        expression = expression.replaceAll("\\)", " )");
        return expression.trim();
    }

    @Command
    @Usage("Roll a dice to get results.")
    @OnExceptionRespond(exception = IllegalArgumentException.class, value = "Something went wrong evaluating that. Check your syntax!")
    public String roll(@Parameter(0) String expression) {
        RollResult result = rollForExpression(expression);
        return result.getPrettyExpression() + " ⟶ " + FormatUtils.bold(result.getResultStr());
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

    public static class RollResult {
        private final Object result;
        private final String prettyExpression;

        public RollResult(Object result, String prettyExpression) {
            this.result = result;
            this.prettyExpression = prettyExpression;
        }

        public Object getResult() {
            return result;
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
