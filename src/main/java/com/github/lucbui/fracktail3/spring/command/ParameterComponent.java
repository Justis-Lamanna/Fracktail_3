package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ParameterComponent {
    ParameterConverterFunction func;
    List<Guard> guards;
    List<CommandGuard> commandGuards;

    public ParameterComponent(ParameterConverterFunction func) {
        this.func = func;
        this.guards = new ArrayList<>();
        this.commandGuards = new ArrayList<>();
    }

    public void addGuard(Guard guard) {
        this.guards.add(guard);
    }

    public void addCommandGuard(CommandGuard guard) {
        this.commandGuards.add(guard);
    }

    public interface ParameterConverterFunction extends Function<CommandUseContext<?>, Object> {}
}
