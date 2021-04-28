package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A piece of a MethodCallingAction or FieldCallingAction which determines how the method's response should be handled
 */
@Data
public class ReturnComponent {
    protected RCFunction func;
    protected final List<Consumer<Object>> consumers;

    public ReturnComponent() {
        this.consumers = new ArrayList<>();
    }

    public interface RCFunction {
        Mono<Void> apply(CommandUseContext context, Object returnVal);
    }
}
