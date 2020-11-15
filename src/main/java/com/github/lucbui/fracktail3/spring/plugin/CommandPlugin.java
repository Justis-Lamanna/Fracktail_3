package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.magic.command.Command;

import java.util.Collections;
import java.util.List;

public interface CommandPlugin extends Plugin {
    default List<Command> addAdditionalCommands() { return Collections.emptyList(); }

    default void onCommandAdd(Command c) {}

    default void onCommandMerge(Command old, Command nuu) {}
}
