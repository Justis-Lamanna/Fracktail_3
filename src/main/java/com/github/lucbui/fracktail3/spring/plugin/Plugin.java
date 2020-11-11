package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.magic.command.Command;

import java.util.List;

public interface Plugin {
    List<Command> addAdditionalCommands();
}
