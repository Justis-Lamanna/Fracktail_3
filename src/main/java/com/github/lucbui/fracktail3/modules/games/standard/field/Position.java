package com.github.lucbui.fracktail3.modules.games.standard.field;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The position on a board
 */
@Data
public class Position {
    private final int row;
    private final int col;

    public static Position parse(String str) {
        if(str.length() != 2) {
            throw new IllegalArgumentException("Str must be <col><row>");
        }
        str = str.toLowerCase();
        char col = str.charAt(0);
        char row = str.charAt(1);
        if(!Character.isDigit(row)) {
            throw new IllegalArgumentException("Str must be <col><row>");
        }
        if(col < 'a' || col > 'z') {
            throw new IllegalArgumentException("Str must be <col><row>");
        }
        return new Position(row - '1', col - 'a');
    }

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

    public List<Position> between(Position other) {
        return IntStream.range(col, other.col)
                .skip(1)
                .mapToObj(indCol -> interpolate(other, indCol))
                .collect(Collectors.toList());
    }

    public Position add(int rows, int cols) {
        return new Position(this.row + rows, this.col + cols);
    }
}
