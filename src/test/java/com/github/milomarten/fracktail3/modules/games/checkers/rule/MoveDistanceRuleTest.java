package com.github.milomarten.fracktail3.modules.games.checkers.rule;

import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.Color;
import com.github.milomarten.fracktail3.modules.games.checkers.Piece;
import com.github.milomarten.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveDistanceRuleTest {
    private static final MoveDistanceRule RULE = new MoveDistanceRule();

    @Test
    public void nonDiagonalMoveIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(piece, start);

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(0, 2));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void singleDiagonalIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(piece, start);

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(1, 2));
        assertTrue(RULE.isLegalMove(ma, board).isLegal());
    }

    @Test
    public void diagonalJumpIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece black = new Piece(Color.BLACK);
        Position start = new Position(0, 1);
        board.addPiece(piece, start);
        board.addPiece(black, new Position(1, 2));

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(2, 3));
        assertTrue(RULE.isLegalMove(ma, board).isLegal());
    }

    @Test
    public void diagonalJumpOverSelfIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece black = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(piece, start);
        board.addPiece(black, new Position(1, 2));

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(2, 3));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void diagonalJumpOverNothingIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(piece, start);

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(2, 3));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void diagonalSoarIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(piece, start);

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(6, 7));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void movingNonexistentPieceIsIllegal() {
        Checkerboard board = new Checkerboard(8);

        MoveAction ma = new MoveAction(Color.RED, new Piece(Color.RED), new Position(6, 7));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void multiJumpIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece black = new Piece(Color.BLACK);
        Piece black2 = new Piece(Color.BLACK);

        Position start = new Position(0, 1);
        Position blackP = new Position(1, 2);
        Position mid = new Position(2, 3);
        Position black2P = new Position(3, 4);
        Position end = new Position(4, 5);

        board.addPiece(piece, start);
        board.addPiece(black, blackP);
        board.addPiece(black2, black2P);

        MoveAction ma = new MoveAction(Color.RED, piece, mid, new Position[]{end});
        assertTrue(RULE.isLegalMove(ma, board).isLegal());
    }

    @Test
    public void multiJumpPartiallyOverNothingIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece black = new Piece(Color.BLACK);

        Position start = new Position(0, 1);
        Position blackP = new Position(1, 2);
        Position mid = new Position(2, 3);
        Position end = new Position(4, 5);

        board.addPiece(piece, start);
        board.addPiece(black, blackP);

        MoveAction ma = new MoveAction(Color.RED, piece, mid, new Position[]{end});
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void multiJumpOverNothingIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);

        Position start = new Position(0, 1);
        Position mid = new Position(2, 3);
        Position end = new Position(4, 5);

        board.addPiece(piece, start);

        MoveAction ma = new MoveAction(Color.RED, piece, mid, new Position[]{end});
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void multiRegularMoveIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);

        Position start = new Position(0, 1);
        Position mid = new Position(1, 2);
        Position end = new Position(3, 4);

        board.addPiece(piece, start);

        MoveAction ma = new MoveAction(Color.RED, piece, mid, new Position[]{end});
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }
}