package com.github.lucbui.fracktail3.spring.command.model;

import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * A base component which determines how the method's response should be handled
 */
@Data
public abstract class ReturnBaseComponent<T> {
    protected ReturnConverterFunction<? super T> func;
    protected final List<Consumer<Object>> consumers;

    /**
     * Initialize this component with a handler
     */
    protected ReturnBaseComponent() {
        this.consumers = new ArrayList<>();
    }

    /**
     * Add an object consumer to this component.
     * After the ReturnConverterFunction is executed, the returned object is trickled through the consumers, performing
     * any external handling (such as logging)
     * @param consumer The consumer to use.
     */
    public void addConsumer(Consumer<Object> consumer) {
        this.consumers.add(consumer);
    }

    /**
     * Describes a function which performs some action when the method or field returns.
     */
    public interface ReturnConverterFunction<T> extends BiFunction<T, Object, Mono<Void>> {}
}
