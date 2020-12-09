package com.github.lucbui.fracktail3.dnd;

import org.apache.commons.lang3.EnumUtils;

import java.util.Optional;

/**
 * The overarching alignment of a character
 */
public enum Alignment {
    LAWFUL_GOOD(Ethical.LAWFUL, Moral.GOOD),
    NEUTRAL_GOOD(Ethical.NEUTRAL, Moral.GOOD),
    CHAOTIC_GOOD(Ethical.CHAOTIC, Moral.GOOD),
    LAWFUL_NEUTRAL(Ethical.LAWFUL, Moral.NEUTRAL),
    TRUE_NEUTRAL(Ethical.NEUTRAL, Moral.NEUTRAL),
    CHAOTIC_NEUTRAL(Ethical.CHAOTIC, Moral.NEUTRAL),
    LAWFUL_EVIL(Ethical.LAWFUL, Moral.EVIL),
    NEUTRAL_EVIL(Ethical.NEUTRAL, Moral.EVIL),
    CHAOTIC_EVIL(Ethical.CHAOTIC, Moral.EVIL);

    private final Ethical ethicalAxis;
    private final Moral moralAxis;

    /**
     * Creates an alignment
     * @param ethicalAxis The ethical axis
     * @param moralAxis The moral axis
     */
    Alignment(Ethical ethicalAxis, Moral moralAxis) {
        this.ethicalAxis = ethicalAxis;
        this.moralAxis = moralAxis;
    }

    /**
     * Get the ethical axis of this alignment
     * @return The ethical axis
     */
    public Ethical getEthicalAxis() {
        return ethicalAxis;
    }

    /**
     * Get the moral axis of this alignment
     * @return The moral axis
     */
    public Moral getMoralAxis() {
        return moralAxis;
    }

    /**
     * Get an alignment given the two axes
     * @param ethical The ethical axis
     * @param moral The moral axis
     * @return The alignment encapsulating these two axes
     */
    public static Alignment getFor(Ethical ethical, Moral moral) {
        return EnumUtils.getEnumList(Alignment.class)
                .stream()
                .filter(align -> align.getEthicalAxis() == ethical && align.getMoralAxis() == moral)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Somehow asked for a nonexistent alignment: " + ethical + " - " + moral));
    }

    /**
     * Get the short name of this alignment
     * @return The short name (LG, for ex)
     */
    public String getShortName() {
        if(ethicalAxis == Ethical.NEUTRAL && moralAxis == Moral.NEUTRAL) {
            return "N";
        } else {
            return ethicalAxis.getShortName() + moralAxis.getShortName();
        }
    }

    /**
     * Get the name of this alignment
     * @return The name (Lawful Good, for ex))
     */
    public String getName() {
        if(ethicalAxis == Ethical.NEUTRAL && moralAxis == Moral.NEUTRAL) {
            return "True Neutral";
        } else {
            return ethicalAxis.getName() + " " + moralAxis.getName();
        }
    }

    /**
     * Get the opposite alignment
     * @return The opposing alignment
     */
    public Alignment getOpposite() {
        return getFor(ethicalAxis.getOpposite(), moralAxis.getOpposite());
    }

    /**
     * Retrieve an alignment by its short name
     * @param name The name to look up (CN, N, LG, etc)
     * @return The alignment, if found
     */
    public static Optional<Alignment> getByShortName(String name) {
        if(name.length() > 2) {
            return Optional.empty();
        } else if(name.length() == 1) {
            return name.equals("N") ? Optional.of(TRUE_NEUTRAL) : Optional.empty();
        } else {
            return EnumUtils.getEnumList(Alignment.class)
                    .stream()
                    .filter(a -> a.getShortName().equals(name))
                    .findFirst();
        }
    }
}
