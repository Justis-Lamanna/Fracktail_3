package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.params.TypeLimits;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    protected List<AdditionalParam> additionalParams;

    /**
     * Initialize this component
     */
    public MethodComponent() {
        this.guards = new ArrayList<>();
        this.transformers = new ArrayList<>();
        this.additionalParams = new ArrayList<>();
    }

    /**
     * Add a guard to this component
     * @param guard The guard to add
     */
    public void addGuard(Guard guard) {
        this.guards.add(guard);
    }

    /**
     * Add a CommandUseContext consumer
     * @param transformer The function which decorates the CommandUseContext
     */
    public void addTransformer(Consumer<CommandUseContext> transformer) {
        transformers.add(transformer);
    }

    /**
     * Add an additional parameter
     * @param param The parameter to add
     */
    public void addAdditionalParam(AdditionalParam param) {
        additionalParams.add(param);
    }

    @Data
    public static class AdditionalParam {
        protected final TypeLimits type;
        protected final int index;
        protected final String name;
        protected final String help;
    }
}
