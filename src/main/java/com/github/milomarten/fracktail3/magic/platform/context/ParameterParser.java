package com.github.milomarten.fracktail3.magic.platform.context;

import com.github.milomarten.fracktail3.magic.command.Command;

public interface ParameterParser {
    Parameters parseParametersFromMessage(Command command, String message);
}
