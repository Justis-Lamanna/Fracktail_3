package com.github.milomarten.fracktail3.modules.games.standard.field;

import com.github.milomarten.fracktail3.modules.games.TestBoard;
import com.github.milomarten.fracktail3.modules.games.TestPiece;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private static final Position ONE_ONE = new Position(1, 1);
    private static final Position ELSEWHERE = new Position(2, 2);
    private Board<TestPiece> board;

    @BeforeEach
    public void createBoard() {
        board = new TestBoard();
        board.addPiece(TestPiece.INSTANCE, ONE_ONE);
    }

    @Test
    public void testRemovePieceSendsToGraveyard() {
        assertTrue(board.removePiece(TestPiece.INSTANCE, ONE_ONE));
        assertEquals(1, board.getGraveyard().size());
    }

    @Test
    public void testRemoveNonexistentPieceDoesNothing() {
        assertFalse(board.removePiece(TestPiece.INSTANCE, ELSEWHERE));
        assertEquals(0, board.getGraveyard().size());
    }

    @Test
    public void testRemovePiecesSendsAllToGraveyard() {
        board.addPiece(TestPiece.INSTANCE, ONE_ONE);
        board.addPiece(TestPiece.INSTANCE, ONE_ONE);

        assertTrue(board.removePieces(ONE_ONE));
        assertEquals(3, board.getGraveyard().size());
    }

    @Test
    public void testRemoveNonexistentPiecesDoesNothing() {
        assertFalse(board.removePieces(ELSEWHERE));
        assertEquals(0, board.getGraveyard().size());
    }

    @Test
    public void testMovePieceMovesPiece() {
        assertTrue(board.movePiece(TestPiece.INSTANCE, ONE_ONE, ELSEWHERE));
        assertEquals(1, board.getPieces(ELSEWHERE).size());
    }

    @Test
    public void testMoveNonexistentPieceDoesNothing() {
        assertFalse(board.movePiece(TestPiece.INSTANCE, ELSEWHERE, ONE_ONE));
    }

    @Test
    public void testGetPositionOfPieceByIdentity() {
        Optional<Position> position = board.getPositionOfPiece(TestPiece.INSTANCE);
        assertTrue(position.isPresent());
        assertEquals(ONE_ONE, position.get());
    }

    @Test
    public void testGetPositionOfNonexistentPieceByIdentity() {
        Optional<Position> position = board.getPositionOfPiece(TestPiece.OTHER);
        assertFalse(position.isPresent());
    }

    @Test
    public void testGetPositionOfPiecesByPredicate() {
        board.addPiece(TestPiece.INSTANCE, ELSEWHERE);
        Set<Position> positions = board.getPositionsOfPieces(TestPiece.INSTANCE::equals);
        assertEquals(SetUtils.hashSet(ONE_ONE, ELSEWHERE), positions);
    }

    @Test
    public void testReinstatePieceRemovesFromGraveyard() {
        board.removePiece(TestPiece.INSTANCE, ONE_ONE);
        assertTrue(board.reinstatePiece(TestPiece.INSTANCE, ELSEWHERE));

        assertEquals(1, board.getPieces(ELSEWHERE).size());
        assertTrue(board.getGraveyard().isEmpty());
    }

    @Test
    public void testReinstateNonexistentPieceDoesNothing() {
        assertFalse(board.reinstatePiece(TestPiece.INSTANCE, ELSEWHERE));

        assertEquals(0, board.getPieces(ELSEWHERE).size());
        assertTrue(board.getGraveyard().isEmpty());
    }

    @Test
    public void testGetOccupiedPositions() {
        board.addPiece(TestPiece.INSTANCE, ELSEWHERE);
        MultiSet<Position> positions = board.getOccupiedPositions();

        assertEquals(1, positions.getCount(ONE_ONE));
        assertEquals(1, positions.getCount(ELSEWHERE));
    }
}