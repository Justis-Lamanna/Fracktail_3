package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

/**
 * A guard which enforces a minimum and maximum size of parameters.
 */
public class ParameterSizeGuard implements Guard {
    private final int minimum;
    private final int maximum;

    /**
     * Initialize
     * @param minimum The minimum number of parameters (inclusive)
     * @param maximum The maximum number of prameters (inclusive)
     */
    public ParameterSizeGuard(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public Mono<Boolean> matches(CommandUseContext ctx) {
        if (ctx instanceof CommandUseContext) {
            CommandUseContext cctx = (CommandUseContext) ctx;
            int size = cctx.getParameters().getNumberOfParameters();
            return Mono.just(size >= minimum && size <= maximum);
        }
        return Mono.just(true);
    }

    /**
     * Get the minimum number of parameters
     * @return The minimum number of parameters
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Get the maximum number of parameters
     * @return The maximum number of parameters
     */
    public int getMaximum() {
        return maximum;
    }
}
