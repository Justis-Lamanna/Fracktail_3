package com.github.lucbui.fracktail3.modules.games;

public class InvalidPositionException extends RuntimeException {
    public InvalidPositionException(Object piece, Position position) {
        super(String.format("Unable to move piece %s to position %s", piece, position));
    }
}
