package com.github.milomarten.fracktail3.magic.platform;

import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.command.Command;
import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;

public interface ContextConstructor {
    CommandUseContext constructContext(Bot bot, Platform platform, Message message, Command command, String parameters);
}
