package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ReturnComponent {
    final ReturnConverterFunction func;
    final List<Consumer<Object>> consumers;

    public ReturnComponent(ReturnConverterFunction func) {
        this.func = func;
        this.consumers = new ArrayList<>();
    }

    public ReturnConverterFunction getFunc() {
        return func;
    }

    public List<Consumer<Object>> getConsumers() {
        return Collections.unmodifiableList(consumers);
    }

    public void addConsumer(Consumer<Object> consumer) {
        this.consumers.add(consumer);
    }

    public interface ReturnConverterFunction extends BiFunction<CommandUseContext<?>, Object, Mono<Void>> {}
}
