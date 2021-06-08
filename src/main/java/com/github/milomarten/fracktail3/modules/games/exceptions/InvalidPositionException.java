package com.github.milomarten.fracktail3.modules.games.exceptions;

import com.github.milomarten.fracktail3.modules.games.standard.field.Position;

public class InvalidPositionException extends RuntimeException {
    public InvalidPositionException(Object piece, Position position) {
        super(String.format("Unable to move piece %s to position %s", piece, position));
    }
}
