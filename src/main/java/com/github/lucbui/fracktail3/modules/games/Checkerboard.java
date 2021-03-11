package com.github.lucbui.fracktail3.modules.games;

import lombok.Data;

@Data
public class Checkerboard<T> extends Board<T> {
    private final int width;
    private final int height;

    @Override
    public boolean isValidPosition(T piece, Position position) {
        return position.getRow() >= 0 &&
                position.getRow() < height &&
                position.getCol() >= 0 &&
                position.getRow() < width;
    }
}
