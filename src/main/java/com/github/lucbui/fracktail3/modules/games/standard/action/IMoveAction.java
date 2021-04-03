package com.github.lucbui.fracktail3.modules.games.standard.action;

import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.standard.field.Board;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;

/**
 * Marks an action as a basic move
 * @param <T>
 */
public interface IMoveAction<GF extends Board<T>, T> extends Action<GF> {
    /**
     * The piece being moved
     * @return The piece
     */
    T getPiece();

    /**
     * The position to move the piece to
     * @return The position to move to
     */
    Position getPosition();
}
