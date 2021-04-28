package com.github.lucbui.fracktail3.spring.command.factory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Useful methods for creating and decorating via strategy
 */
public class FactoryUtils {
    public static <COMP, STRAT> COMP decorate(List<STRAT> strategies, BiFunction<STRAT, COMP, COMP> decorateFunc, COMP seed) {
        COMP current = seed;
        for(STRAT s : strategies) {
            current = decorateFunc.apply(s, current);
        }
        return current;
    }
}
