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

    public boolean isOrthogonal(Position other) {
        return row == other.row || col == other.col;
    }

    public boolean isAdjacent(Position other) {
        return isDistance(other, 1);
    }

    public boolean isDistance(Position other, int distance) {
        return Math.abs(other.row - row) == distance || Math.abs(other.col - col) == distance;
    }

    public Position interpolate(Position other, int independentCol) {
        int dependentRow = row + ((independentCol - col) * ((other.col - col) / (other.row - row)));
        return new Position(dependentRow, independentCol);
    }

    public Position interpolate(Position other, double weight) {
        int calculatedIndCol = (int) (((weight * other.col) + ((1.0 - weight) * col)) / 1.0);
        return interpolate(other, calculatedIndCol);
    }
}
