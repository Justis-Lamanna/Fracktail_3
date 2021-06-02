package com.github.lucbui.fracktail3.magic.command;

import lombok.Data;

import java.util.Set;

@Data
public class CommandSpec {
    private final Set<String> names;
    private final String help;
}
