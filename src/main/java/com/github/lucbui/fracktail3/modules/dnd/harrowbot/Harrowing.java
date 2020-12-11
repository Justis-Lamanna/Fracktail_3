package com.github.lucbui.fracktail3.modules.dnd.harrowbot;

import com.github.lucbui.fracktail3.modules.dnd.AbilityScore;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The result of a Harrowing
 */
public class Harrowing<CARD> {
    public static final int HARROWING_SIZE = 9;

    private final AbilityScore score;
    private final List<CARD> pulls;

    private Harrowing(AbilityScore score, List<CARD> pulls) {
        this.score = score;
        this.pulls = pulls;
    }

    /**
     * Perform a harrowing
     * @param choosing The AbilityScore related to the question
     * @return The result of the Harrowing
     */
    public static <CARD> Harrowing<CARD> perform(AbilityScore choosing, HarrowDeck<CARD> deck) {
        deck.shuffle();
        List<CARD> pulls = IntStream.range(0, HARROWING_SIZE)
                .mapToObj(x -> deck.draw())
                .collect(Collectors.toList());
        return new Harrowing<>(choosing, pulls);
    }

    /**
     * Get the selected AbilityScore
     * @return The ability chosen at the beginning
     */
    public AbilityScore getScore() {
        return score;
    }

    /**
     * Get the nine cards pulled
     * @return The cards pulled
     */
    public List<CARD> getPulls() {
        return Collections.unmodifiableList(pulls);
    }
}
