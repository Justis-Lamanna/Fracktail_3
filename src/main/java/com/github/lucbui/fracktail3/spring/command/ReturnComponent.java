package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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

    public void addConsumer(Consumer<Object> consumer) {
        this.consumers.add(consumer);
    }

    public interface ReturnConverterFunction extends BiFunction<CommandUseContext<?>, Object, Mono<Void>> {}
}
