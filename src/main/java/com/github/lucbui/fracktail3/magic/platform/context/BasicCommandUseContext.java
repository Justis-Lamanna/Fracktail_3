package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import lombok.Data;

@Data
public class BasicCommandUseContext implements CommandUseContext {
    private final Bot bot;
    private final Platform platform;
    private final Message message;
    private final Command command;
    private final Parameters parameters;
}
