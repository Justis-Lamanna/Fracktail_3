package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.guard.Guard;

import java.util.ArrayList;
import java.util.List;

public class MethodComponent {
    List<Guard> guards;
    List<CommandGuard> commandGuards;

    public MethodComponent() {
        this.guards = new ArrayList<>();
        this.commandGuards = new ArrayList<>();
    }

    public void addGuard(Guard guard) {
        this.guards.add(guard);
    }

    public void addCommandGuard(CommandGuard guard) {
        this.commandGuards.add(guard);
    }
}
