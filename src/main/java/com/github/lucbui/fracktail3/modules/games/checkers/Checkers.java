package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.ActionLimitRule;
import com.github.lucbui.fracktail3.modules.games.BasicGame;
import com.github.lucbui.fracktail3.modules.games.Position;
import com.github.lucbui.fracktail3.modules.games.Rule;
import com.github.lucbui.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.lucbui.fracktail3.modules.games.checkers.action.PassAction;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.CorrectPieceRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.MoveDirectionRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.MoveDistanceRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.TurnOrderRule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Checkers extends BasicGame<Checkerboard> {
    private static final List<Rule<Checkerboard>> RULES = Arrays.asList(
            new ActionLimitRule<>(MoveAction.class, PassAction.class),
            new TurnOrderRule(),
            new CorrectPieceRule(),
            new MoveDistanceRule(),
            new MoveDirectionRule()
    );

    public Checkers() {
        super(RULES, createBoard());
    }

    protected static Checkerboard createBoard() {
        Checkerboard board = new Checkerboard(8);
        IntStream.of(0, 1, 2).forEach(row -> createRow(board, row, Color.RED));
        IntStream.of(5, 6, 7).forEach(row -> createRow(board, row, Color.BLACK));
        return board;
    }

    private static void createRow(Checkerboard board, int row, Color color) {
        int offset = row % 2 == 0 ? 1 : 0;
        for(int idx = offset; idx < board.getWidth(); idx += 2) {
            board.addPiece(new Piece(color), new Position(row, idx));
        }
    }
}
