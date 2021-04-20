package com.github.lucbui.fracktail3.modules.games.checkers.rule;

import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Color;
import com.github.lucbui.fracktail3.modules.games.checkers.Piece;
import com.github.lucbui.fracktail3.modules.games.checkers.Type;
import com.github.lucbui.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveDirectionRuleTest {
    private static final MoveDirectionRule RULE = new MoveDirectionRule();

    @Test
    public void redMoveDownIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(4, 5);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.RED, piece, new Position(5, 4));
        assertTrue(RULE.isLegalMove(action, board).isLegal());
    }

    @Test
    public void redMoveUpIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(4, 5);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.RED, piece, new Position(3, 4));
        assertTrue(RULE.isLegalMove(action, board).isIllegal());
    }

    @Test
    public void redKingMoveUpIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED, Type.KING);
        Position start = new Position(4, 5);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.RED, piece, new Position(3, 4));
        assertTrue(RULE.isLegalMove(action, board).isLegal());
    }

    @Test
    public void blackMoveUpIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.BLACK);
        Position start = new Position(4, 5);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.BLACK, piece, new Position(3, 4));
        assertTrue(RULE.isLegalMove(action, board).isLegal());
    }

    @Test
    public void blackMoveDownIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.BLACK);
        Position start = new Position(4, 5);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.BLACK, piece, new Position(5, 4));
        assertTrue(RULE.isLegalMove(action, board).isIllegal());
    }

    @Test
    public void blackKingMoveDownIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.BLACK, Type.KING);
        Position start = new Position(4, 5);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.BLACK, piece, new Position(5, 4));
        assertTrue(RULE.isLegalMove(action, board).isLegal());
    }

    @Test
    public void redMoveDownIntoKingAndBackUpIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(6, 1);
        Position middle = new Position(7, 2);
        Position end = new Position(6, 3);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.RED, piece, middle, new Position[]{end});
        assertTrue(RULE.isLegalMove(action, board).isLegal());
        assertEquals(Type.KING, piece.getType());
    }

    @Test
    public void redMoveDownAndUpIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(4, 5);
        Position middle = new Position(5, 4);
        Position end = new Position(4, 3);
        board.addPiece(piece, start);

        MoveAction action = new MoveAction(Color.RED, piece, middle, new Position[]{end});
        assertTrue(RULE.isLegalMove(action, board).isIllegal());
    }
}