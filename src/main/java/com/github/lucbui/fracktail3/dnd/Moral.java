package com.github.lucbui.fracktail3.dnd;

import org.apache.commons.lang3.EnumUtils;

import java.util.Optional;

/**
 * The morality axis of a character's alignment
 */
public enum Moral {
    /**
     * Good - expresses kindness and healing
     */
    GOOD("G", "Good"),

    /**
     * Neutral - expresses both Good and Evil tendencies
     */
    NEUTRAL("N", "Neutral"),

    /**
     * Evil - expresses hatred and pain
     */
    EVIL("E", "Evil");

    private final String shortName;
    private final String name;

    Moral(String shortName, String name) {
        this.shortName = shortName;
        this.name = name;
    }

    /**
     * Get the short name of this axis
     * @return The short name (G, N, or E)
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Get the name of this axis
     * @return The name (Good, Neutral, or Evil)
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the opposite Moral alignment
     * @return The oppositing Moral alignment
     */
    public Moral getOpposite() {
        switch (this) {
            case EVIL: return GOOD;
            case GOOD: return EVIL;
            default: return NEUTRAL;
        }
    }

    /**
     * Retrieve an moral alignment by short name
     * @param sName The short name to look up
     * @return The matching axis, if any matched
     */
    public static Optional<Moral> getByShortName(String sName) {
        return EnumUtils.getEnumList(Moral.class)
                .stream()
                .filter(e -> e.shortName.equals(sName))
                .findFirst();
    }
}
