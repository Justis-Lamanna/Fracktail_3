package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * A piece of a MethodCallingAction which resolves the context into a parameter to be injected into the method
 */
public class ParameterComponent {
    ParameterConverterFunction func;
    List<Guard> guards;

    /**
     * Initialize this component with a function
     * @param func The function to use
     */
    public ParameterComponent(ParameterConverterFunction func) {
        this.func = func;
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
     * Get the ParameterConverterFunction being used
     * @return The function used
     */
    public ParameterConverterFunction getFunc() {
        return func;
    }

    /**
     * Get the parameter-specific guards being used
     * @return The guards used
     */
    public List<Guard> getGuards() {
        return Collections.unmodifiableList(guards);
    }

    /**
     * A function which converts the CommandUseContext into some injectable parameter
     */
    public interface ParameterConverterFunction extends Function<CommandUseContext<?>, Object> {}
}
