package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A piece of a MethodCallingAction which resolves the context into a parameter to be injected into the method
 */
@Getter
@Setter
public class ParameterComponent {
    protected final TypeDescriptor type;
    protected PCFunction func;
    protected String name;
    protected String help;
    protected boolean optional;
    protected List<Guard> guards;

    public ParameterComponent(TypeDescriptor type, String name) {
        this.type = type;
        this.name = name;
        this.guards = new ArrayList<>();
    }

    public ParameterComponent(TypeDescriptor type) {
        this.type = type;
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
