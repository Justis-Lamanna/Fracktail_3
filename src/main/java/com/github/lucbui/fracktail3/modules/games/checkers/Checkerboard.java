package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.standard.field.Board;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import com.github.lucbui.fracktail3.modules.games.standard.field.TurnBasedGameField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.EnumUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A specific type of board, made of a grid of squares
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Checkerboard extends Board<Piece> implements TurnBasedGameField<Color>, Iterable<Piece> {
    private final int width;
    private final int height;
    private int currentPlayer = 0;
    private Color forfeitedPlayer;

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
    public void advanceTurnTo(Color color) {
        currentPlayer = color.getTurnOrder();
    }

    public Color getCurrentPlayer() {
        return Color.getByTurnOrder(currentPlayer);
    }

    @Override
    public Collection<Color> getAllPlayers() {
        return EnumUtils.getEnumList(Color.class);
    }

    /**
     * Get the current standings, if there is one
     * @return A map of how many pieces each color has in play
     */
    public Map<Color, Long> getStandings() {
        return getPieces().stream()
                .collect(Collectors.groupingBy(Piece::getColor, Collectors.counting()));
    }

    @Override
    public Iterator<Piece> iterator() {
        return new Iterator<Piece>() {
            int row;
            int col;

            @Override
            public boolean hasNext() {
                return row != getBottomRow() && col != getRightColumn();
            }

            @Override
            public Piece next() {
                col++;
                if(col == width) {
                    col = 0;
                    row++;
                }
                Collection<Piece> pieces = getPieces(new Position(row, col));
                return pieces.size() == 0 ? null : pieces.iterator().next();
            }
        };
    }
}
