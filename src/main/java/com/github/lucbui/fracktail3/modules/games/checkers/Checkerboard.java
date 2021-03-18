package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.Board;
import com.github.lucbui.fracktail3.modules.games.HasTurns;
import com.github.lucbui.fracktail3.modules.games.Position;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A specific type of board, made of a grid of squares
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Checkerboard extends Board<Piece> implements HasTurns {
    private final int width;
    private final int height;
    private int currentPlayer = 0;

    /**
     * Create a square checkerboard, of dimension width and height
     * @param dimension The width and height of the board
     */
    public Checkerboard(int dimension) {
        this.width = dimension;
        this.height = dimension;
    }

    @Override
    public boolean isValidPosition(Piece piece, Position position) {
        return position.getRow() >= 0 &&
                position.getRow() < height &&
                position.getCol() >= 0 &&
                position.getRow() < width;
    }

    @Override
    public void advanceTurn() {
        currentPlayer = (currentPlayer + 1) % 2;
    }

    public Color getCurrentPlayerColor() {
        return getCurrentPlayer() == 0 ? Color.RED : Color.BLACK;
    }
}
