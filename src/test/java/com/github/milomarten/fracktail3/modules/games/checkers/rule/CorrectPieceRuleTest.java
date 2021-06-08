package com.github.milomarten.fracktail3.modules.games.checkers.rule;

import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.Color;
import com.github.milomarten.fracktail3.modules.games.checkers.Piece;
import com.github.milomarten.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CorrectPieceRuleTest {
    private static final CorrectPieceRule RULE = new CorrectPieceRule();

    @Test
    public void redMovingRedPieceIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece red = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(red, start);

        MoveAction action = new MoveAction(Color.RED, red, new Position(1, 2));

        assertTrue(RULE.isLegalMove(action, board).isLegal());
    }

    @Test
    public void redMovingBlackIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece red = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(red, start);

        MoveAction action = new MoveAction(Color.BLACK, red, new Position(1, 2));

        assertFalse(RULE.isLegalMove(action, board).isLegal());
    }
}