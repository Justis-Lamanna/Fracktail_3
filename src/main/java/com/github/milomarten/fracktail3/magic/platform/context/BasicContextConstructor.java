package com.github.milomarten.fracktail3.magic.platform.context;

import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.command.Command;
import com.github.milomarten.fracktail3.magic.platform.ContextConstructor;
import com.github.milomarten.fracktail3.magic.platform.Message;
import com.github.milomarten.fracktail3.magic.platform.Platform;
import lombok.Data;

@Data
public class BasicContextConstructor implements ContextConstructor {
    private final ParameterParser parameterParser;

    @Override
    public CommandUseContext constructContext(Bot bot, Platform platform, Message message, Command command, String parameters) {
        return new BasicCommandUseContext(bot, platform, message, message.getSender(), message.getOrigin(), command,
                parameterParser.parseParametersFromMessage(command, parameters));
    }
}
