package com.github.milomarten.fracktail3.modules.games.standard.rule;

import com.github.milomarten.fracktail3.modules.games.checkers.Checkerboard;
import com.github.milomarten.fracktail3.modules.games.checkers.Color;
import com.github.milomarten.fracktail3.modules.games.checkers.Piece;
import com.github.milomarten.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.milomarten.fracktail3.modules.games.standard.field.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveToEmptySpaceRuleTest {
    private static final MoveToEmptySpaceRule RULE = new MoveToEmptySpaceRule();

    @Test
    public void moveToEmptyIsLegal() {
        Checkerboard board = new Checkerboard(8);
        Piece red = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(red, start);

        MoveAction ma = new MoveAction(Color.RED, red, new Position(1, 2));
        assertTrue(RULE.isLegalMove(ma, board).isLegal());
    }

    @Test
    public void moveToNonEmptyIsIllegal() {
        Checkerboard board = new Checkerboard(8);
        Piece red = new Piece(Color.RED);
        Position start = new Position(0, 1);
        board.addPiece(red, start);
        board.addPiece(new Piece(Color.RED), new Position(1, 2));

        MoveAction ma = new MoveAction(Color.RED, red, new Position(1, 2));
        assertTrue(RULE.isLegalMove(ma, board).isIllegal());
    }
}