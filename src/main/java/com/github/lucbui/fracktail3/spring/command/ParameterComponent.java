package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ParameterComponent {
    Function<CommandUseContext<?>, Object> func;
    List<Guard> guards;

    public ParameterComponent(Function<CommandUseContext<?>, Object> func) {
        this.func = func;
        this.guards = new ArrayList<>();
    }

    public void addGuard(Guard guard) {
        this.guards.add(guard);
    }
}
