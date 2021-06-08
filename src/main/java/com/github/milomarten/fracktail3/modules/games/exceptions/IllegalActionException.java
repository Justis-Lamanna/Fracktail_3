package com.github.milomarten.fracktail3.modules.games.exceptions;

import com.github.milomarten.fracktail3.modules.games.ActionLegality;

public class IllegalActionException extends RuntimeException {
    public IllegalActionException(ActionLegality whyNot) {
        super("Could not perform action:\n" + whyNot.getReason());
    }
}
