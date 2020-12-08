package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class ExceptionComponent {
    final Map<Class<? extends Throwable>, ExceptionHandler> candidates;

    public ExceptionComponent() {
        this.candidates = new HashMap<>();
    }

    public void addHandler(Class<? extends Throwable> clazz, ExceptionHandler handler) {
        this.candidates.put(clazz, handler);
    }

    public Map<Class<? extends Throwable>, ExceptionHandler> getCandidates() {
        return Collections.unmodifiableMap(candidates);
    }

    public Optional<ExceptionHandler> getBestHandlerFor(Class<? extends Throwable> clazz) {
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

    public interface ExceptionHandler extends BiFunction<CommandUseContext<?>, Throwable, Mono<Void>> {}
}
