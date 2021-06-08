package com.github.milomarten.fracktail3.modules.games.checkers.action;

import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.Color;
import com.github.milomarten.fracktail3.modules.games.checkers.Piece;
import com.github.milomarten.fracktail3.modules.games.checkers.Type;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveActionTest {
    @Test
    public void testStandardMoveAction() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Position start = new Position(0, 1);
        Position end = new Position(1, 2);
        board.addPiece(piece, start);
        MoveAction action = new MoveAction(Color.RED, piece, end);

        action.performAction(board);

        Assertions.assertFalse(board.getPiece(start).isPresent());
        Assertions.assertEquals(piece, board.getPiece(end).orElseGet(Assertions::fail));
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

        Assertions.assertEquals(Type.KING, piece.getType());
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

        Assertions.assertFalse(board.getPiece(start).isPresent());
        Assertions.assertFalse(board.getPiece(middle).isPresent());
        Assertions.assertEquals(piece, board.getPiece(end).orElseGet(Assertions::fail));
        Assertions.assertEquals(1, board.getGraveyard().size());
    }

    @Test
    public void testMultiJump() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece toJump = new Piece(Color.BLACK);
        Piece secondJump = new Piece(Color.BLACK);
        Position start = new Position(0, 1);
        Position toJumpPosition = new Position(1, 2);
        Position middle = new Position(2, 3);
        Position secondJumpPosition = new Position(3, 4);
        Position end = new Position(4, 5);

        board.addPiece(piece, start);
        board.addPiece(toJump, toJumpPosition);
        board.addPiece(secondJump, secondJumpPosition);

        MoveAction action = new MoveAction(Color.RED, piece, middle, new Position[]{end});

        action.performAction(board);
        Assertions.assertFalse(board.getPiece(start).isPresent());
        Assertions.assertFalse(board.getPiece(middle).isPresent());
        Assertions.assertEquals(piece, board.getPiece(end).orElseGet(Assertions::fail));
        Assertions.assertEquals(2, board.getGraveyard().size());
    }
}