package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.command.Command;

public interface ParameterParser {
    Parameters parseParametersFromMessage(Command command, String message);
}
