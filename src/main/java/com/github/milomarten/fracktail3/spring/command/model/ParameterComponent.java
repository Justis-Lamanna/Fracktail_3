package com.github.milomarten.fracktail3.spring.command.model;

import com.github.milomarten.fracktail3.magic.guard.Guard;
import com.github.milomarten.fracktail3.magic.params.TypeLimits;
import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A piece of a MethodCallingAction which resolves the context into a parameter to be injected into the method
 */
@Getter
@Setter
public class ParameterComponent {
    protected TypeLimits type;
    protected int index;
    protected PCFunction func;
    protected String name;
    protected String help;
    protected List<Guard> guards;

    public ParameterComponent() {
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

    @FunctionalInterface
    public interface PCFunction {
        Object apply(CommandUseContext context);
    }
}
