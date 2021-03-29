package com.github.lucbui.fracktail3.modules.games.checkers;

import java.util.function.Function;

/**
 * Checkers piece color
 */
public enum Color {
    RED {
        @Override
        public Function<Checkerboard, Integer> getPromoteRow() {
            return Checkerboard::getBottomRow;
        }
    },
    BLACK {
        @Override
        public Function<Checkerboard, Integer> getPromoteRow() {
            return Checkerboard::getTopRow;
        }
    };

    /**
     * Get the turn order for the color
     * @return 0 or 1, representing turn order
     */
    public int getTurnOrder() {
        return this.ordinal();
    }

    public abstract Function<Checkerboard, Integer> getPromoteRow();

    /**
     * Get color by turn order
     * @param turnOrder The turn order, 0 or 1
     * @return The color
     */
    public static Color getByTurnOrder(int turnOrder) {
        return Color.values()[turnOrder];
    }
}
