package com.github.milomarten.fracktail3.modules.games.checkers.rule;

import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.Color;
import com.github.milomarten.fracktail3.modules.games.checkers.Piece;
import com.github.milomarten.fracktail3.modules.games.checkers.Type;
import com.github.milomarten.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MustJumpRuleTest {
    private static final MustJumpRule RULE = new MustJumpRule();

    @Test
    public void nonCaptureIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece opp = new Piece(Color.BLACK);
        Position piecePos = new Position(4, 5);
        Position oppPos = new Position(5, 4);

        board.addPiece(piece, piecePos);
        board.addPiece(opp, oppPos);

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(5, 6));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }

    @Test
    public void nonCaptureIsLegalIfImpossible() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED);
        Piece opp = new Piece(Color.BLACK);
        Position piecePos = new Position(4, 5);
        Position oppPos = new Position(3, 4);

        board.addPiece(piece, piecePos);
        board.addPiece(opp, oppPos);

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(5, 6));
        assertTrue(RULE.isLegalMove(ma, board).isLegal());
    }

    @Test
    public void nonCaptureIsIllegalIfKing() {
        Checkerboard board = new Checkerboard(8);
        Piece piece = new Piece(Color.RED, Type.KING);
        Piece opp = new Piece(Color.BLACK);
        Position piecePos = new Position(4, 5);
        Position oppPos = new Position(3, 4);

        board.addPiece(piece, piecePos);
        board.addPiece(opp, oppPos);

        MoveAction ma = new MoveAction(Color.RED, piece, new Position(5, 6));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }
}