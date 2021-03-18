package com.github.lucbui.fracktail3.modules.games;

import lombok.Data;

/**
 * The position on a board
 */
@Data
public class Position {
    private final int row;
    private final int col;

    public boolean isDiagonal(Position other) {
        return Math.abs(other.row - row) == Math.abs(other.col - col);
    }

    public boolean isOrthogonalTo(Position other) {
        return row == other.row || col == other.col;
    }

    public boolean isAdjacentTo(Position other) {
        return isDistanceFrom(other, 1);
    }

    public boolean isDistanceFrom(Position other, int distance) {
        return Math.abs(other.row - row) == distance || Math.abs(other.col - col) == distance;
    }

    public boolean isLeftOf(Position other) {
        return this.col < other.col;
    }

    public boolean isRightOf(Position other) {
        return this.col > other.col;
    }

    public boolean isAbove(Position other) {
        return this.row < other.row;
    }

    public boolean isBelow(Position other) {
        return this.row > other.row;
    }

    public Position interpolate(Position other, int independentCol) {
        int dependentRow = row + ((independentCol - col) * ((other.col - col) / (other.row - row)));
        return new Position(dependentRow, independentCol);
    }

    public Position middle(Position other) {
        int calculatedIndCol = (other.col + col) / 2;
        return interpolate(other, calculatedIndCol);
    }
}
