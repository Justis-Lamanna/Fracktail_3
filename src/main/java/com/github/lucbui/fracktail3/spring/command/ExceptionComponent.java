package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class ExceptionComponent {
    final Map<Class<? extends Throwable>, BiFunction<CommandUseContext<?>, Throwable, Mono<Void>>> candidates;

    public ExceptionComponent() {
        this.candidates = new HashMap<>();
    }

    public void addHandler(Class<? extends Throwable> clazz,  BiFunction<CommandUseContext<?>, Throwable, Mono<Void>> handler) {
        this.candidates.put(clazz, handler);
    }

    public Optional<BiFunction<CommandUseContext<?>, Throwable, Mono<Void>>> getBestHandlerFor(Class<? extends Throwable> clazz) {
        if(candidates.isEmpty()) return Optional.empty(); //Don't even bother.
        Class<?> current = clazz;
        //Iterate through the class tree until we find a suitable match.
        while(current != Exception.class) {
            if (candidates.containsKey(current)) {
                return Optional.of(candidates.get(current));
            }
            current = current.getSuperclass();
        }
        return Optional.empty();
    }
}
