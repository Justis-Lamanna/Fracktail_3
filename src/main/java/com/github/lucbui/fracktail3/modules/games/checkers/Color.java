package com.github.lucbui.fracktail3.modules.games.checkers;

/**
 * Checkers piece color
 */
public enum Color {
    RED,
    BLACK;

    /**
     * Get the turn order for the color
     * @return 0 or 1, representing turn order
     */
    public int getTurnOrder() {
        return this.ordinal();
    }

    /**
     * Get color by turn order
     * @param turnOrder The turn order, 0 or 1
     * @return The color
     */
    public static Color getByTurnOrder(int turnOrder) {
        return Color.values()[turnOrder];
    }
}
