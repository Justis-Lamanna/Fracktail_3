package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.standard.field.Board;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import com.github.lucbui.fracktail3.modules.games.standard.field.TurnBasedGameField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Collection;

/**
 * A specific type of board, made of a grid of squares
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Checkerboard extends Board<Piece> implements TurnBasedGameField<Integer> {
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

    public int getTopRow() { return 0; }
    public int getBottomRow() { return this.height - 1; }
    public int getLeftColumn() { return 0; }
    public int getRightColumn() { return this.width - 1; }

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

    @Override
    public void advanceTurnTo(Integer playerIdx) {
        currentPlayer = playerIdx;
    }

    public Color getCurrentPlayerColor() {
        return getCurrentPlayer() == 0 ? Color.RED : Color.BLACK;
    }

    @Override
    public Collection<Integer> getAllPlayers() {
        return Arrays.asList(0, 1);
    }
}
