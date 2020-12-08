package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.guard.Guard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodComponent {
    List<Guard> guards;

    public MethodComponent() {
        this.guards = new ArrayList<>();
    }

    public void addGuard(Guard guard) {
        this.guards.add(guard);
    }

    public List<Guard> getGuards() {
        return Collections.unmodifiableList(guards);
    }
}
