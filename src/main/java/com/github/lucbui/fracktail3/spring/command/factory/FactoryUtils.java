package com.github.lucbui.fracktail3.spring.command.factory;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Useful methods for creating and decorating via strategy
 */
public class FactoryUtils {
    public static <COMP, STRAT> Optional<COMP> createAndDecorate(List<STRAT> strategies,
                                            Function<STRAT, Optional<COMP>> createFunc,
                                            BiFunction<STRAT, COMP, COMP> decorateFunc) {
        for(STRAT s : strategies) {
            Optional<COMP> componentOpt = createFunc.apply(s);
            if(componentOpt.isPresent()) {
                return Optional.of(decorate(strategies, decorateFunc, componentOpt.get()));
            }
        }
        return Optional.empty();
    }

    public static <COMP, STRAT> COMP decorate(List<STRAT> strategies, BiFunction<STRAT, COMP, COMP> decorateFunc, COMP seed) {
        COMP current = seed;
        for(STRAT s : strategies) {
            current = decorateFunc.apply(s, current);
        }
        return current;
    }
}
