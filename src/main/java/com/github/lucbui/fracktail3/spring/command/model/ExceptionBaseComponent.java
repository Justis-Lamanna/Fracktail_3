package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A base component which handles exceptions thrown in the course of execution
 */
public abstract class ExceptionBaseComponent<T extends BaseContext<?>> {
    final Map<Class<? extends Throwable>, ExceptionHandler<? super T>> candidates;

    /**
     * Initialize this component
     */
    public ExceptionBaseComponent() {
        this.candidates = new HashMap<>();
    }

    /**
     * Add a handler to this component.
     * Handlers will also handle any subclasses, if they are thrown, and there is no more specific handler to use.
     * @param clazz The exception class this handler handles
     * @param handler The handler to use
     */
    public void addHandler(Class<? extends Throwable> clazz, ExceptionHandler<? super T> handler) {
        this.candidates.put(clazz, handler);
    }

    /**
     * Get all candidates and their corresponding handler
     * @return An unmodifiable map of each throwable class and its handler
     */
    public Map<Class<? extends Throwable>, ExceptionHandler<? super T>> getCandidates() {
        return Collections.unmodifiableMap(candidates);
    }

    /**
     * Retrieve the best handler to handle the specified class.
     * Recursively step up the input class's parents until a match is found. If none are found,
     * empty is returned.
     * @param clazz The throwable class to attempt to handle
     * @return The ExceptionHandler for that class, or empty if none are available
     */
    public Optional<ExceptionHandler<? super T>> getBestHandlerFor(Class<? extends Throwable> clazz) {
        if(candidates.isEmpty()) return Optional.empty(); //Don't even bother.
        Class<?> current = clazz;
        //Iterate through the class tree until we find a suitable match.
        while(current != null && current != Object.class) {
            if (candidates.containsKey(current)) {
                return Optional.of(candidates.get(current));
            }
            current = current.getSuperclass();
        }
        return Optional.empty();
    }

    /**
     * Describes a handler which accepts the CommandUseContext and Throwable
     */
    public interface ExceptionHandler<T extends BaseContext<?>> extends BiFunction<T, Throwable, Mono<Void>> {}
}
