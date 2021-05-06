package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A piece of a MethodCallingAction or FieldCallingAction which handles top-level guards
 */
@Getter
@Setter
public class MethodComponent {
    protected String id;
    protected Set<String> names;
    protected String help;
    protected List<Guard> guards;
    protected List<Consumer<CommandUseContext>> transformers;

    /**
     * Initialize this component
     */
    public MethodComponent() {
        this.guards = new ArrayList<>();
        this.transformers = new ArrayList<>();
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

    /**
     * Add a CommandUseContext consumer
     * @param transformer The function which decorates the CommandUseContext
     */
    public void addTransformer(Consumer<CommandUseContext> transformer) {
        transformers.add(transformer);
    }
}
