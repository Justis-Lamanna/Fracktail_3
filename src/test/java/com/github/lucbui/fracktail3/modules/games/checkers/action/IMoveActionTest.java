package com.github.lucbui.fracktail3.modules.games.checkers.action;

import com.github.lucbui.fracktail3.modules.games.checkers.Checkerboard;
import com.github.lucbui.fracktail3.modules.games.checkers.Color;
import com.github.lucbui.fracktail3.modules.games.checkers.Piece;
import com.github.lucbui.fracktail3.modules.games.checkers.Type;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IMoveActionTest {
    @Test
    public void testStandardMoveAction() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(0, 1);
        Position end = new Position(1, 2);
        board.addPiece(piece, start);
        MoveAction action = new MoveAction(Color.RED, piece, end);

        action.performAction(board);

        assertFalse(board.getPiece(start).isPresent());
        assertEquals(piece, board.getPiece(end).orElseGet(Assertions::fail));
    }

    @Test
    public void testMoveToKing() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(6, 1);
        Position end = new Position(7, 2);
        board.addPiece(piece, start);
        MoveAction action = new MoveAction(Color.RED, piece, end);

        action.performAction(board);

        assertEquals(Type.KING, piece.getType());
    }

    @Test
    public void testJump() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece toJump = new Piece(Color.BLACK);
        Position start = new Position(0, 1);
        Position middle = new Position(1, 2);
        Position end = new Position(2, 3);
        board.addPiece(piece, start);
        board.addPiece(toJump, middle);

        MoveAction action = new MoveAction(Color.RED, piece, end);

        action.performAction(board);

        assertFalse(board.getPiece(start).isPresent());
        assertFalse(board.getPiece(middle).isPresent());
        assertEquals(piece, board.getPiece(end).orElseGet(Assertions::fail));
        assertEquals(1, board.getGraveyard().size());
    }
}