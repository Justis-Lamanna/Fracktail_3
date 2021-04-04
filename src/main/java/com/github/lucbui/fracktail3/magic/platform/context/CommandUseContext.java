package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Message;

public interface CommandUseContext<T> extends PlatformBaseContext<T> {
    Message getMessage();
    Command getCommand();
    Parameters getParameters();
}
