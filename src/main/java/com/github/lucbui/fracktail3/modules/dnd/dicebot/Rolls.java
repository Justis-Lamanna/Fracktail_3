package com.github.lucbui.fracktail3.modules.dnd.dicebot;

import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Rolls {
    private final List<Integer> rolls;
    private final int keep;
    private final boolean lowest;

    private Rolls(List<Integer> rolls, int keep, boolean lowest) {
        this.rolls = rolls;
        this.keep = keep;
        this.lowest = lowest;
    }

    public static Rolls rollDice(Dice dice) {
        return rollDice(dice, 1, false, 1);
    }

    public static Rolls rollDice(Dice dice, int numberOfRolls) {
        return rollDice(dice, numberOfRolls, false, numberOfRolls);
    }

    public static Rolls rollDice(Dice dice, int numberOfRolls, boolean lowest, int keep) {
        List<Integer> rolls = IntStream.range(0, numberOfRolls)
                .map(x -> dice.roll())
                .boxed()
                .collect(Collectors.toList());
        return new Rolls(rolls, keep, lowest);
    }

    public List<Integer> getRolls() {
        return rolls;
    }

    public int getKeep() {
        return keep;
    }

    public boolean isLowest() {
        return lowest;
    }

    private Stream<Integer> getRollsStream() {
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
        return getRollsStream().map(i -> Integer.toString(i))
                .collect(Collectors.joining("+", "(", ")"));
    }

    public String getPrettyExpression() {
        if(keep > 0) {
            MultiSet<Integer> keptRolls = getRollsStream().collect(Collectors.toCollection(HashMultiSet::new));
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
            return getRollsStream()
                    .map(i -> Integer.toString(i))
                    .collect(Collectors.joining("+", "(", ")"));
        }
    }
}
