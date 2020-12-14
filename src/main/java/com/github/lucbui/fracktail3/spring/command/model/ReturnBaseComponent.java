package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class ReturnBaseComponent<T extends BaseContext<?>> {
    final ReturnConverterFunction<? super T> func;
    final List<Consumer<Object>> consumers;

    protected ReturnBaseComponent(ReturnConverterFunction<? super T> func) {
        this.func = func;
        this.consumers = new ArrayList<>();
    }

    public ReturnConverterFunction<? super T> getFunc() {
        return func;
    }

    public List<Consumer<Object>> getConsumers() {
        return Collections.unmodifiableList(consumers);
    }

    public void addConsumer(Consumer<Object> consumer) {
        this.consumers.add(consumer);
    }

    public interface ReturnConverterFunction<T extends BaseContext<?>> extends BiFunction<T, Object, Mono<Void>> {}
}
