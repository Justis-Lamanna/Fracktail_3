package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.resolver.Resolver;

import java.util.Collections;
import java.util.List;

public class Command {
    private final Resolver<String> name;
    private final Resolver<List<String>> aliases;
    //private final List<Behavior> behaviors;
    //private final Behavior orElse;


    public Command(Resolver<String> name, Resolver<List<String>> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public Resolver<String> getName() {
        return name;
    }

    public Resolver<List<String>> getAliases() {
        return aliases;
    }
}
