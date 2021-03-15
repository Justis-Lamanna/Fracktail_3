package com.github.lucbui.fracktail3.modules.games;

import lombok.Data;

/**
 * A specific type of board, made of a grid of squares
 * @param <T> The type of pieces on the board
 */
@Data
public class Checkerboard<T> extends Board<T> {
    private final int width;
    private final int height;

    /**
     * Create a square checkerboard, of dimension width and height
     * @param dimension The width and height of the board
     */
    public Checkerboard(int dimension) {
        this.width = dimension;
        this.height = dimension;
    }

    @Override
    public boolean isValidPosition(T piece, Position position) {
        return position.getRow() >= 0 &&
                position.getRow() < height &&
                position.getCol() >= 0 &&
                position.getRow() < width;
    }
}
