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
public class ParameterComponent extends ParameterBaseComponent<CommandUseContext> {
    protected String name;
    protected String help;
    protected boolean optional;
    protected List<Guard> guards;

    public ParameterComponent(TypeDescriptor type, ParameterConverterFunction<? super CommandUseContext> func, String name) {
        super(type, func);
        this.name = name;
        this.guards = new ArrayList<>();
    }

    public ParameterComponent(TypeDescriptor type, ParameterConverterFunction<? super CommandUseContext> func) {
        super(type, func);
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
