package com.github.milomarten.fracktail3.modules.dnd;

import org.apache.commons.lang3.EnumUtils;

import java.util.Optional;

/**
 * The ethicality axis of a character's alignment
 */
public enum Ethical {
    /**
     * Lawful - Follows rules and order
     */
    LAWFUL("L", "Lawful"),

    /**
     * Neutral - Follows both Lawful and Chaotic tendencies
     */
    NEUTRAL("N", "Neutral"),

    /**
     * Chaotic - Follows whatever the way
     */
    CHAOTIC("C", "Chaotic");

    private final String shortName;
    private final String name;

    Ethical(String shortName, String name) {
        this.shortName = shortName;
        this.name = name;
    }

    /**
     * Get the short name of this axis
     * @return The short name (L, N, or C)
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Get the name of this axis
     * @return The name (Lawful, Neutral, or Chaotic)
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the opposite Ethical alignment
     * @return The oppositing Ethical alignment
     */
    public Ethical getOpposite() {
        switch(this) {
            case LAWFUL: return CHAOTIC;
            case CHAOTIC: return LAWFUL;
            default: return NEUTRAL;
        }
    }

    /**
     * Retrieve an ethical alignment by short name
     * @param sName The short name to look up
     * @return The matching axis, if any matched
     */
    public static Optional<Ethical> getByShortName(String sName) {
        return EnumUtils.getEnumList(Ethical.class)
                .stream()
                .filter(e -> e.shortName.equals(sName))
                .findFirst();
    }
}
