package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.BasicGame;
import com.github.lucbui.fracktail3.modules.games.Rule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.CorrectPieceRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.MoveDirectionRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.MoveDistanceRule;
import com.github.lucbui.fracktail3.modules.games.checkers.rule.TurnOrderRule;

import java.util.Arrays;
import java.util.List;

public class Checkers extends BasicGame<Checkerboard> {
    private static final List<Rule<Checkerboard>> RULES = Arrays.asList(
            new TurnOrderRule(),
            new CorrectPieceRule(),
            new MoveDistanceRule(),
            new MoveDirectionRule()
    );

    public Checkers() {
        super(RULES, new Checkerboard(8));
    }
}
