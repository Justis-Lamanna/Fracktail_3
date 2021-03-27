package com.github.lucbui.fracktail3.modules.games.standard.field;

import com.github.lucbui.fracktail3.modules.games.exceptions.InvalidPositionException;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.*;
import java.util.function.Predicate;

/**
 * Represents a board of pieces in specific positions.
 * In addition to the board, pieces that have been removed from play (such as by capture) are maintained.
 * @param <T> The type of pieces on the board
 */
@EqualsAndHashCode
public abstract class Board<T> {
    protected MultiValuedMap<Position, T> board;
    protected List<T> removals;

    /**
     * Create an empty board
     */
    public Board() {
        this.board = new ArrayListValuedHashMap<>();
        this.removals = new ArrayList<>();
    }

    /**
     * Create a copy of a board from an existing one
     * @param initialBoard The board to copy
     */
    public Board(Board<T> initialBoard) {
        this.board = new ArrayListValuedHashMap<>(initialBoard.board);
        this.removals = new ArrayList<>(initialBoard.removals);
    }

    /**
     * Get all pieces at a position
     * @param position The position to get
     * @return The pieces at that position
     */
    public Collection<T> getPieces(Position position) {
        return board.get(position);
    }

    /**
     * Add a piece to the board
     * @param piece The piece to add
     * @param position The position on the board to add it
     * @return True, if the piece was placed
     * @throws InvalidPositionException The position the piece was attempted to be placed was invalid
     */
    public boolean addPiece(T piece, Position position) {
        if(!isValidPosition(piece, position)) {
            throw new InvalidPositionException(piece, position);
        }
        return board.put(position, piece);
    }

    /**
     * Returns a piece back to the board
     * @param piece The piece to reinstate
     * @param position The position to place it
     * @return True, if the piece was actually in the graveyard + returned to the board
     */
    public boolean reinstatePiece(T piece, Position position) {
        if(!isValidPosition(piece, position)) {
            throw new InvalidPositionException(piece, position);
        }
        if(removals.remove(piece)) {
            return addPiece(piece, position);
        }
        return false;
    }

    /**
     * Remove a piece from the board
     * @param piece The piece to remove
     * @param position The position of the piece
     * @return True, if the piece was removed successfully (it was present in that location on the board)
     */
    public boolean removePiece(T piece, Position position) {
        boolean wasRemoved = board.removeMapping(position, piece);
        if(wasRemoved) {
            removals.add(piece);
        }
        return wasRemoved;
    }

    /**
     * Remove all pieces at the specified position
     * @param position The position to clear
     * @return True, if any pieces were removed
     */
    public boolean removePieces(Position position) {
        Collection<T> removed = board.remove(position);
        if(CollectionUtils.isNotEmpty(removed)) {
            removals.addAll(removed);
            return true;
        }
        return false;
    }

    /**
     * Move a piece from one position to another
     * @param piece The piece to move
     * @param start The starting position of the piece
     * @param end The ending position of the piece
     * @return True, if the piece was moved
     * @throws InvalidPositionException The position the piece was attempted to be placed was invalid
     */
    public boolean movePiece(T piece, Position start, Position end) {
        if(!isValidPosition(piece, end)) {
            throw new InvalidPositionException(piece, end);
        }
        if(board.containsMapping(start, piece)) {
            board.removeMapping(start, piece);
            board.put(end, piece);
            return true;
        }
        return false;
    }

    /**
     * Get the position of a piece
     * @param piece The piece to find
     * @return The position, if it exists, or an empty Optional otherwise
     */
    public Optional<Position> getPositionOfPiece(T piece) {
        return getPositionOfPiece(p -> Objects.equals(p, piece));
    }

    /**
     * Get the position of a piece, via predicate
     * @param piecePredicate The predicate to determine piece eligibility
     * @return The position of a piece that matches the predicate
     */
    public Optional<Position> getPositionOfPiece(Predicate<T> piecePredicate) {
        MapIterator<Position, T> iter = board.mapIterator();
        while(iter.hasNext()) {
            Position pos = iter.next();
            T pieceAtPos = iter.getValue();
            if(piecePredicate.test(pieceAtPos)) {
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }

    /**
     * Get the position of all pieces that match a predicate
     * @param piecePredicate The predicate to determine piece eligibility
     * @return The positions of all pieces that matches the predicate
     */
    public List<Position> getPositionsOfPieces(Predicate<T> piecePredicate) {
        MapIterator<Position, T> iter = board.mapIterator();
        List<Position> foundPositions = new ArrayList<>();
        while(iter.hasNext()) {
            Position pos = iter.next();
            T pieceAtPos = iter.getValue();
            if(piecePredicate.test(pieceAtPos)) {
                foundPositions.add(pos);
            }
        }
        return foundPositions;
    }

    /**
     * Get all pieces removed from play
     * @return The pieces removed from play
     */
    public List<T> getGraveyard() {
        return Collections.unmodifiableList(removals);
    }

    /**
     * Get all pieces on the board
     * @return The pieces on the board
     */
    public Collection<T> getPieces() {
        return Collections.unmodifiableCollection(board.values());
    }

    /**
     * Get all occupied positions
     * @return The occupied positions
     */
    public MultiSet<Position> getOccupiedPositions() {
        return board.keys();
    }

    public abstract boolean isValidPosition(T piece, Position position);
}
