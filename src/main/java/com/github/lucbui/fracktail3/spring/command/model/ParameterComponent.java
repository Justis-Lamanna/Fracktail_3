package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A piece of a MethodCallingAction which resolves the context into a parameter to be injected into the method
 */
public class ParameterComponent extends ParameterBaseComponent<CommandUseContext<?>> {
    List<Guard> guards;

    /**
     * Initialize this component with a function
     * @param func The function to use
     */
    public ParameterComponent(ParameterConverterFunction<CommandUseContext<?>> func) {
        super(func);
        this.guards = new ArrayList<>();
    }

    /**
     * Add a parameter-specific guard to this component
     * @param guard The guard to add
     */
    public void addGuard(Guard guard) {
        this.guards.add(guard);
    }

    /**
     * Get the parameter-specific guards being used
     * @return The guards used
     */
    public List<Guard> getGuards() {
        return Collections.unmodifiableList(guards);
    }
}
