package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * A piece of a MethodCallingAction or FieldCallingAction which determines how the method's response should be handled
 */
public class ReturnComponent {
    final ReturnConverterFunction func;
    final List<Consumer<Object>> consumers;

    /**
     * Initialize this component with a handler
     * @param func A function which does something with the value returned from the method or field
     */
    public ReturnComponent(ReturnConverterFunction func) {
        this.func = func;
        this.consumers = new ArrayList<>();
    }

    /**
     * Get the ReturnConverterFunction used
     * @return the ReturnConverterFunction used
     */
    public ReturnConverterFunction getFunc() {
        return func;
    }

    /**
     * Get the consumers of the object used
     * @return The object consumers
     */
    public List<Consumer<Object>> getConsumers() {
        return Collections.unmodifiableList(consumers);
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
    public interface ReturnConverterFunction extends BiFunction<CommandUseContext<?>, Object, Mono<Void>> {}
}
