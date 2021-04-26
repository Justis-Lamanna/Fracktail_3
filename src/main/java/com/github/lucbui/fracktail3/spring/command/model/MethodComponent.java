package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A piece of a MethodCallingAction or FieldCallingAction which handles top-level guards
 */
@Getter
@Setter
public class MethodComponent {
    protected String id;
    protected Set<String> names;
    protected String help;
    List<Guard> guards;

    /**
     * Initialize this component
     */
    public MethodComponent() {
        this.guards = new ArrayList<>();
    }

    /**
     * Add a guard to this component
     * @param guard The guard to add
     */
    public void addGuard(Guard guard) {
        this.guards.add(guard);
    }

    /**
     * Get a list of guards in this component
     * @return The guards of this component
     */
    public List<Guard> getGuards() {
        return Collections.unmodifiableList(guards);
    }
}
