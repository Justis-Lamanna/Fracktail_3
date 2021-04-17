package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.BasicGame;
import com.github.lucbui.fracktail3.modules.games.Rule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.CorrectPieceRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.MoveDirectionRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.MoveDistanceRule;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import com.github.lucbui.fracktail3.modules.games.standard.rule.MoveToEmptySpaceRule;
import com.github.lucbui.fracktail3.modules.games.standard.rule.TurnOrderRule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Checkers extends BasicGame<Checkerboard> {
    private static final List<Rule<Checkerboard>> RULES = Arrays.asList(
            new TurnOrderRule<>(),
            new CorrectPieceRule(),
            new MoveDistanceRule(),
            new MoveDirectionRule(),
            new MoveToEmptySpaceRule<>()
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

    @Override
    public boolean isComplete() {
        Checkerboard field = getGameField();
        if(field.getForfeitedPlayer() != null) {
            return true;
        }

        Map<Color, Long> standings = field.getStandings();
        return standings.get(Color.RED) == 0 || standings.get(Color.BLACK) == 0;
    }
}
