package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.Position;
import lombok.Data;

import java.util.List;

@Data
public class MoveAction {
    private final Piece piece;
    private final List<Position> positions;
}
