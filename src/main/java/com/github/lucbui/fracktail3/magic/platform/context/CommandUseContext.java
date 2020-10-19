package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.command.Command;

public interface CommandUseContext<T> extends BaseContext<T> {
    Command getCommand();
    String getRawParameters();
    String[] getParameters();
}
